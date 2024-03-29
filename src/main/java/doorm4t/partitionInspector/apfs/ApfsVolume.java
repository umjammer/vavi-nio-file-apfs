/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.apfs;

import doorm4t.partitionInspector.apfs.bTreeNode.BTreeNode;
import doorm4t.partitionInspector.apfs.kv.*;
import doorm4t.partitionInspector.apfs.kv.keys.DRECKey;
import doorm4t.partitionInspector.apfs.kv.keys.EXTENTKey;
import doorm4t.partitionInspector.apfs.kv.keys.FSObjectKeyFactory;
import doorm4t.partitionInspector.apfs.kv.values.DRECValue;
import doorm4t.partitionInspector.apfs.kv.values.EXTENTValue;
import doorm4t.partitionInspector.utils.Tuple;
import doorm4t.partitionInspector.utils.Utils;
import vavi.util.serdes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class ApfsVolume {
    // "root" dir will always have an inode number of 1
    // See APFS reference pg 96
    public static final long ROOT_DIR_OID = 1L;

    // DREC record flags will tell us whether an entry is a Directory or a regular File.
    // See APFS reference page 100
    int DT_DIR = 4;
    int DT_FILE = 8;

    private int blockSize;
    private String imagePath;

    // Volume fields
    public BlockHeader blockHeader;

    @Element(sequence = 1)
    public byte[] apfs_magic = new byte[4];
    @Element(sequence = 2)
    public int apfs_fs_index;

    @Element(sequence = 3)
    public long apfs_features;
    @Element(sequence = 4)
    public long apfs_readonly_compatible_features;
    @Element(sequence = 5)
    public long apfs_incompatible_features;

    @Element(sequence = 6)
    public long apfs_unmount_time;

    @Element(sequence = 7)
    public long apfs_fs_reserve_block_count;
    @Element(sequence = 8)
    public long apfs_fs_quota_block_count;
    @Element(sequence = 9)
    public long apfs_fs_alloc_count;

    @Element(sequence = 10)
    public short wrapped_crypto_state_t_major_version;
    @Element(sequence = 11)
    public short wrapped_crypto_state_t_minor_version;
    @Element(sequence = 12)
    public int wrapped_crypto_state_t_cpflags;
    @Element(sequence = 13)
    public int wrapped_crypto_state_t_persistent_class;
    @Element(sequence = 14)
    public int wrapped_crypto_state_t_key_os_version;
    @Element(sequence = 15)
    public short wrapped_crypto_state_t_key_revision;
    @Element(sequence = 16)
    public short wrapped_crypto_state_t_key_len;

    @Element(sequence = 17)
    public int apfs_root_tree_oid_type;
    @Element(sequence = 18)
    public int apfs_extentref_tree_oid_type;
    @Element(sequence = 19)
    public int apfs_snap_meta_tree_oid_type;

    @Element(sequence = 20)
    public long apfs_omap_oid;
    @Element(sequence = 21)
    public long apfs_root_tree_oid;
    @Element(sequence = 22)
    public long apfs_extentref_tree_oid;
    @Element(sequence = 23)
    public long apfs_snap_meta_tree_oid;

    @Element(sequence = 24)
    public long apfs_revert_to_xid;
    @Element(sequence = 25)
    public long apfs_revert_to_sblock_oid;

    @Element(sequence = 26)
    public long apfs_next_obj_id;

    @Element(sequence = 27)
    public long apfs_num_files;
    @Element(sequence = 28)
    public long apfs_num_directories;
    @Element(sequence = 29)
    public long apfs_num_symlinks;
    @Element(sequence = 30)
    public long apfs_num_other_fsobjects;
    @Element(sequence = 31)
    public long apfs_num_snapshots;
    @Element(sequence = 32)
    public long apfs_total_blocks_alloced;
    @Element(sequence = 33)
    public long apfs_total_blocks_freed;

    @Element(sequence = 34)
    public byte[] apfs_vol_uuid = new byte[16];
    @Element(sequence = 35)
    public long apfs_last_mod_time;

    @Element(sequence = 36)
    public long apfs_fs_flags;

    @Element(sequence = 37)
    public byte[] apfs_modified_by_t_formatted_by_id = new byte[32];
    @Element(sequence = 38)
    public long apfs_modified_by_t_formatted_by_timestamp;
    @Element(sequence = 39)
    public long apfs_modified_by_t_formatted_by_last_xid;
    @Element(sequence = 40)
    public byte[] apfs_modified_by_t_modified_by_id = new byte[32];
    @Element(sequence = 41)
    public long apfs_modified_by_t_modified_by_timestamp;
    @Element(sequence = 42)
    public long apfs_modified_by_t_modified_by_last_xid;
    @Element(sequence = 43)
    public byte[] apfs_modified_by_t_modified_by_1_7 = new byte[336];

    @Element(sequence = 44)
    public byte[] apfs_volname = new byte[256];
    @Element(sequence = 45)
    public int apfs_next_doc_id;

    @Element(sequence = 46)
    public short apfs_role;
    @Element(sequence = 47)
    public short apfs_reserved;

    @Element(sequence = 48)
    public long apfs_root_to_xid;
    @Element(sequence = 49)
    public long apfs_er_state_oid;

    public OMap volumeOMap;

    public HashMap<Long, FSKeyValue> inodeRecords = new HashMap<>();
    public HashMap<Long, FSKeyValue> extentRecords = new HashMap<>();
    public HashMap<Long, ArrayList<FSKeyValue>> drecRecords = new HashMap<>();
    public ArrayList<Tuple<String, EXTENTValue>> files;

    public ApfsVolume() {}

    public ApfsVolume(ByteBuffer buffer, int blockSize, String imagePath) throws IOException {
        this.blockSize = blockSize;
        this.imagePath = imagePath;

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        blockHeader = new BlockHeader(buffer);
        buffer.get(apfs_magic);
        apfs_fs_index = buffer.getInt();
        apfs_features = buffer.getLong();
        apfs_readonly_compatible_features = buffer.getLong();
        apfs_incompatible_features = buffer.getLong();
        apfs_unmount_time = buffer.getLong();
        apfs_fs_reserve_block_count = buffer.getLong();
        apfs_fs_quota_block_count = buffer.getLong();
        apfs_fs_alloc_count = buffer.getLong();
        wrapped_crypto_state_t_major_version = buffer.getShort();
        wrapped_crypto_state_t_minor_version = buffer.getShort();
        wrapped_crypto_state_t_cpflags = buffer.getInt();
        wrapped_crypto_state_t_persistent_class = buffer.getInt();
        wrapped_crypto_state_t_key_os_version = buffer.getInt();
        wrapped_crypto_state_t_key_revision = buffer.getShort();
        wrapped_crypto_state_t_key_len = buffer.getShort();
        apfs_root_tree_oid_type = buffer.getInt();
        apfs_extentref_tree_oid_type = buffer.getInt();
        apfs_snap_meta_tree_oid_type = buffer.getInt();
        apfs_omap_oid = buffer.getLong();
        apfs_root_tree_oid = buffer.getLong();
        apfs_extentref_tree_oid = buffer.getLong();
        apfs_snap_meta_tree_oid = buffer.getLong();
        apfs_revert_to_xid = buffer.getLong();
        apfs_revert_to_sblock_oid = buffer.getLong();
        apfs_next_obj_id = buffer.getLong();
        apfs_num_files = buffer.getLong();
        apfs_num_directories = buffer.getLong();
        apfs_num_symlinks = buffer.getLong();
        apfs_num_other_fsobjects = buffer.getLong();
        apfs_num_snapshots = buffer.getLong();
        apfs_total_blocks_alloced = buffer.getLong();
        apfs_total_blocks_freed = buffer.getLong();
        buffer.get(apfs_vol_uuid);
        apfs_last_mod_time = buffer.getLong();
        apfs_fs_flags = buffer.getLong();
        buffer.get(apfs_modified_by_t_formatted_by_id);
        apfs_modified_by_t_formatted_by_timestamp = buffer.getLong();
        apfs_modified_by_t_formatted_by_last_xid = buffer.getLong();
        buffer.get(apfs_modified_by_t_modified_by_id);
        apfs_modified_by_t_modified_by_timestamp = buffer.getLong();
        apfs_modified_by_t_modified_by_last_xid = buffer.getLong();
        buffer.get(apfs_modified_by_t_modified_by_1_7);
        buffer.get(apfs_volname);
        apfs_next_doc_id = buffer.getInt();
        apfs_role = buffer.getShort();
        apfs_reserved = buffer.getShort();
        apfs_root_to_xid = buffer.getLong();
        apfs_er_state_oid = buffer.getLong();
        volumeOMap = getVolumeOMap();
        files = getFiles();
    }

    public void extractFile(int fileId) throws IOException {
        Tuple<String, EXTENTValue> fileToExtract = files.get(fileId);
        EXTENTValue fileExtent = fileToExtract.y;
        File file = new File(fileToExtract.x);
        String filePath = file.getName();
        Utils.extentRangeToFile(imagePath, filePath, fileExtent.physBlockNum * blockSize, fileExtent.length);
    }

    public void extractAllFiles() throws IOException {
        for (Tuple<String, EXTENTValue> fileToExtract : files) {
            EXTENTValue fileExtent = fileToExtract.y;
            Utils.extentRangeToFile(imagePath, fileToExtract.x, fileExtent.physBlockNum * blockSize, fileExtent.length);
        }
    }

    private ArrayList<Tuple<String, EXTENTValue>> getFiles() {
        ArrayList<Tuple<String, EXTENTValue>> files = new ArrayList<>();
        // TODO: Finish file structure parsing
        // Start parsing from Root Directory
        ArrayDeque<Tuple<FSKeyValue, String>> queue = new ArrayDeque<>();
        ArrayList<FSKeyValue> possible_roots = drecRecords.get(ROOT_DIR_OID);
        FSKeyValue root = null;
        for (FSKeyValue fsKeyValue : possible_roots) {
            DRECValue value = (DRECValue) fsKeyValue.value;
            if (value.fileId == 2) {
                root = fsKeyValue;
                break;
            }
        }

        queue.add(new Tuple<>(root, "/"));
        while (!queue.isEmpty()) {
            Tuple<FSKeyValue, String> tuple = queue.removeFirst();
            FSKeyValue curr = tuple.x;
            String path = tuple.y;

            DRECKey key = (DRECKey) curr.key;
            DRECValue value = (DRECValue) curr.value;

            File folder = new File("./output" + path);
            if (!folder.exists()) {
//                folder.mkdir();
            }

            if (value.flags == DT_FILE) {
                // No new files to add since this record is a Regular File
                // Find related extent
                FSKeyValue extentKv = extentRecords.get(value.fileId);
                EXTENTKey extentKey = (EXTENTKey) extentKv.key;
                EXTENTValue extentValue = (EXTENTValue) extentKv.value;

                // Parse file from the extent
                String name = new String(key.name);
                name = name.substring(0, name.length() - 1); // Remove null terminator character
                String fileOutPath = "./output" + path + name;
                files.add(new Tuple<>(fileOutPath, extentValue));
//                Utils.extentRangeToFile(imagePath, fileOutPath, extentValue.physBlockNum * blockSize, extentValue.length);

                continue;
            }

            // Add new files to the queue since this record is a Directory
            ArrayList<FSKeyValue> children = drecRecords.get(value.fileId);
            if (children != null) {
                for (FSKeyValue child : children) {
                    String name = new String(key.name);
                    name = name.substring(0, name.length() - 1); // Remove null terminator character
                    queue.add(new Tuple<>(child, path + name + "/"));
                }
            }
        }
        return files;
    }

    private OMap getVolumeOMap() throws IOException {
        // Parse the VSB OMap
        // Example using "Many Files.dmg":
        // 0                [1028, 1139]
        // 1  [1028, 1030 to 1139]   [1139 to 1145]
        //  -contains BTree Nodes with OMAP keys (oids 1028 and 1139)
        //          1028's children are Leaf Nodes: oids 1028, 1030 to 1139
        //          1139's children are Leaf Nodes: oids 1139 to 1145
        //              We want to add each leaf node to the OMAP BTree -- their paddr's point us to actual FS Objects
        int vsbOMapOffset = (int) apfs_omap_oid * blockSize;
        ByteBuffer vsbOMapBuffer = Utils.GetBuffer(imagePath, vsbOMapOffset, blockSize);
        OMap volumeOMap = new OMap(vsbOMapBuffer, imagePath, blockSize);


        // Parse BTree Nodes to get all inode, extent, drec
        // Organize FS Objects by record object identifiers
        // TODO: Find a better way to traverse the FS Object Tree
        // Right now, we read ALL the nodes in the VSB OMAP since it looks like they're all FS Object Nodes anyways.
        // This might not be the proper way, but it works for both "bigandsmall.dmg" and "Many Files.dmg"

        for (Long addr : volumeOMap.parsedOmap.values()) {
            int offset = addr.intValue() * blockSize;
            ByteBuffer fsObjNodeBuff = Utils.GetBuffer(imagePath, offset, blockSize * 2);
            BTreeNode node = new BTreeNode(fsObjNodeBuff);

            for (FSKeyValue fskv : node.fsKeyValues) {
                int type = (int) fskv.key.hdr.obj_type;
                switch (type) {
                    case FSObjectKeyFactory.KEY_TYPE_INODE:
                        inodeRecords.put(fskv.key.hdr.obj_id, fskv);
                        break;
                    case FSObjectKeyFactory.KEY_TYPE_EXTENT:
                        extentRecords.put(fskv.key.hdr.obj_id, fskv);
                        break;
                    case FSObjectKeyFactory.KEY_TYPE_DREC:
                        if (!drecRecords.containsKey(fskv.key.hdr.obj_id)) {
                            drecRecords.put(fskv.key.hdr.obj_id, new ArrayList<>());
                        }
                        drecRecords.get(fskv.key.hdr.obj_id).add(fskv);
                        break;
                }
            }
        }
        return volumeOMap;
    }

    @Override
    public String toString() {
        return String.format("APFS Volume\t| Name: %s\nBlock Header %s\nMagic: %s\nOMAP OID: %s\nRoot Tree OID: %s\nExtent Ref Tree OID: %s\nSnap Metatree OID: %s\nNumber of Directories: %s\nNumber of Files: %s\nTotal Allocated Blocks: %s\nTotal Free Blocks: %s\nOMap: %s\n", new String(apfs_volname).replaceAll("\0", ""), blockHeader, new String(apfs_magic), apfs_omap_oid, apfs_root_tree_oid, apfs_extentref_tree_oid, apfs_snap_meta_tree_oid, apfs_num_directories, apfs_num_files, apfs_total_blocks_alloced, apfs_total_blocks_freed, volumeOMap.toString());
    }
}


