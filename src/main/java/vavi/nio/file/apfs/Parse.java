
package vavi.nio.file.apfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import vavi.nio.file.apfs.kaitai.Apfs;
import vavi.nio.file.apfs.kaitai.Apfs.ApfsSuperblockT;
import vavi.nio.file.apfs.kaitai.Apfs.BtreeNodePhysT;
import vavi.nio.file.apfs.kaitai.Apfs.NodeEntry;
import vavi.nio.file.apfs.kaitai.Apfs.NxSuperblockT;
import vavi.nio.file.apfs.kaitai.Apfs.Obj;
import vavi.nio.file.apfs.kaitai.Apfs.OmapValT;

import io.kaitai.struct.KaitaiStream;


/**
 * parse file system
 * 
 * <pre>
 * Parse:
 * 
 * 1. Parse container superblock.
 *     3. Parse OMAP.
 *     4. Iterate over volumes.
 *         5. Get volume superblocks from OMAP.
 *             6. Parse volume superblock.
 *                 7. Parse OMAP.
 *                 8. Go to root directory.
 *                     9. Parse Root directory. Parse required entries.
 *     10. Go to previous container superblock. Recurse 1.
 * </pre>
 */
public class Parse {

    Logger LOGGER = Logger.getLogger(Parse.class.getName());

    /**
     * @param volume_override nullable
     */
    static Map<Long, Map<String, List<NodeEntry>>> add_file_entries(Map<Long, Map<String, List<NodeEntry>>> file_entries,
                                                                    Map<Long, Map<String, List<NodeEntry>>> new_file_entries,
                                                                    long xid_override,
                                                                    String volume_override) {
        for (long xid : new_file_entries.keySet()) {
            if (file_entries.get(xid_override) == null) {
                file_entries.put(xid_override, new HashMap<>());
            }
            for (String volume : new_file_entries.get(xid).keySet()) {
                volume_override = volume_override != null ? volume_override : volume;
                if (file_entries.get(xid_override).get(volume_override) == null) {
                    file_entries.get(xid_override).put(volume_override, new ArrayList<>());
                }
                file_entries.get(xid_override).get(volume_override).addAll(new_file_entries.get(xid).get(volume));
            }
        }
        return file_entries;
    }

    /**
     * 'unknown' is the default volume name node_type 1 contains only pointer
     * records
     */
    static Map<Long, Map<String, List<NodeEntry>>> parse_node(Obj node, KaitaiStream image_io) {
        Map<Long, Map<String, List<NodeEntry>>> map1 = new HashMap<>();
        Map<String, List<NodeEntry>> map2 = new HashMap<>();
        if (((BtreeNodePhysT) node.body()).btnFlags() == 1) {
            map2.put("unknown", new ArrayList<>());
            map1.put(node.hdr().oXid().val(), map2);
        } else {
            map2.put("unknown", ((BtreeNodePhysT) node.body()).btnData());
            map1.put(node.hdr().oXid().val(), map2);
        }
        return map1;
    }

    static Map<Long, Map<String, List<NodeEntry>>> parse_apsb(Obj apsb, KaitaiStream image_io) {
        Map<Long, Map<String, List<NodeEntry>>> file_entries = new HashMap<>();

        for (NodeEntry omap_entry : Low.get_apsb_objects(apsb)) {
            // get root directory
            Obj root_node = ((OmapValT) omap_entry.val()).ovPaddr().target();
            Map<Long, Map<String, List<NodeEntry>>> new_file_entries = parse_node(root_node, image_io);
            file_entries = add_file_entries(file_entries,
                                            new_file_entries,
                                            apsb.hdr().oXid().val(),
                                            ((ApfsSuperblockT) apsb.body()).apfsVolname());
        }
        return file_entries;
    }

    static Map<Long, Map<String, List<NodeEntry>>> parse_nxsb(Obj nxsb, KaitaiStream image_io) {
        Map<Long, Map<String, List<NodeEntry>>> file_entries = new HashMap<>();
//System.err.printf("parse_nxsb: %s\n", nxsb);

//System.err.printf("get_nxsb_objects: %s\n", Low.get_nxsb_objects(nxsb));
        for (NodeEntry fs_entry : Low.get_nxsb_objects(nxsb)) {
            // get volume superblock
            Obj apsb = ((OmapValT) fs_entry.val()).ovPaddr().target();
System.err.printf("apsb: %s\n", nxsb);
            Map<Long, Map<String, List<NodeEntry>>> new_file_entries = parse_apsb(apsb, image_io);
            file_entries = add_file_entries(file_entries, new_file_entries, nxsb.hdr().oXid().val(), null);
        }

        return file_entries;
    }

    /** parse image and print files */
    public static Map<Long, Map<String, List<NodeEntry>>> parse(KaitaiStream image_io) throws IOException {

        Apfs apfs = new Apfs(image_io);

        // get from container superblock
        Obj nxsb = apfs.block0();
//System.err.println("nxsb: " + nxsb);
//System.err.println("nxsb.body(): " + nxsb.body());
        long block_size = ((NxSuperblockT) nxsb.body()).nxBlockSize();
System.err.printf("block_size: %d\n", block_size);
        Map<Long, Map<String, List<NodeEntry>>> file_entries = parse_nxsb(nxsb, image_io);
        long prev_nxsb = ((NxSuperblockT) nxsb.body()).nxXpDescBase() + ((NxSuperblockT) nxsb.body()).nxXpDescIndex() + 1;
        long count = ((NxSuperblockT) nxsb.body()).nxXpDescLen();

        // get from older container superblocks
        for (int i = 0; i < count; i++) {
            byte[] data = Block.get_block((int) prev_nxsb, (int) block_size, image_io);
            nxsb = new Obj(new ByteBufferKaitaiStream2(data), apfs, apfs);
            try {
                file_entries.putAll(parse_nxsb(nxsb, image_io));
                prev_nxsb = ((NxSuperblockT) nxsb.body()).nxXpDescBase() + ((NxSuperblockT) nxsb.body()).nxXpDescIndex() + 1;
            } catch (Exception e) {
                break;
            }
        }

        return file_entries;
    }
}
