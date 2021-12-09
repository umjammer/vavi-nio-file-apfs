package vavi.nio.file.apfs;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import vavi.nio.file.apfs.kaitai.Apfs;
import vavi.nio.file.apfs.kaitai.Apfs.NodeEntry;
import vavi.nio.file.apfs.kaitai.Apfs.XFieldT;
import vavi.util.ByteUtil;
import vavi.util.Debug;

import io.kaitai.struct.KaitaiStream;


/**
 * Multiple output functions
 */
public class Process {

    static Logger logger = Logger.getLogger(Process.class.getName());

    String get_path(Map<Long, Map<String, Object>> itemmap, long fid) {
        if (itemmap.containsKey(fid)) {
            String name = itemmap.get(fid).get("name") != null ? (String) itemmap.get(fid).get("name") : "???" + fid + "???";
            String parent_path = (long) itemmap.get(fid).get("parent") > 1 ? get_path(itemmap, (long) itemmap.get(fid).get("parent")) : "";
            return parent_path + "/" + name;
        }
        return "???_UNKNOWN_???";
    }

    Map<String, Object> process_extent(Map<String, Object> extent, long remaining, int blocksize, KaitaiStream file_io, MessageDigest md5) throws IOException {
        for (int block_part = 0; block_part < (long) (extent.get("length")) / blocksize; block_part++) {
            byte[] data = Block.get_block((int) ((long) extent.get("start")) + block_part, blocksize, file_io);
            long chunk_size;
            if (remaining < blocksize) {
                chunk_size = remaining;
            } else {
                chunk_size = blocksize;
            }
            remaining -= chunk_size;
            md5.update(data, 0, (int) chunk_size);
        }
        long r = remaining;
        Map<String, Object> m = new HashMap<String, Object>() {{
            put("md5", md5);
            put("remaining", r);
        }};
        return m;
    }

    /** Print file entries as table */
    public ItemStore process_file_entries(Map<Long, Map<String, List<NodeEntry>>> file_entries, Apfs apfs, int blocksize, KaitaiStream file_io) throws Exception {

        Map<Long, Map<String, Map<Long, List<Map<String, Object>>>>> extentmap = new HashMap<>();
        Map<Long, Map<String, Map<Long, Map<String, Object>>>> itemmap = new HashMap<>();

        for (long xid : file_entries.keySet()) {

            extentmap.put(xid, new HashMap<>());
            itemmap.put(xid, new HashMap<>());

            for (String volume : file_entries.get(xid).keySet()) {

                extentmap.get(xid).put(volume, new HashMap<>());
                itemmap.get(xid).put(volume, new HashMap<>());

                for (NodeEntry file_entry : file_entries.get(xid).get(volume)) {
                    // try {
                    if (file_entry.jKeyT().objType() == Apfs.JObjTypes.APFS_TYPE_FILE_EXTENT &&
                            file_entry.val() instanceof Apfs.JExtentValT) {

                        if (extentmap.get(xid).get(volume).get((long) file_entry.jKeyT().objId()) == null) {
                            extentmap.get(xid).get(volume).put((long) file_entry.jKeyT().objId(), new ArrayList<>());
                        }
                        Map<String, Object> m = new HashMap<String, Object>() {{
                            put("start", ((Apfs.JExtentValT) file_entry.val()).physBlockNum());
                            put("length", ((Apfs.JExtentValT) file_entry.val()).len());
                            put("offset", ((Apfs.JExtentKeyT) file_entry.key()).offset());
                        }};
                        extentmap.get(xid).get(volume).get((long) file_entry.jKeyT().objId()).add(m);

System.err.println("extent: " + xid);

                    } else if (file_entry.jKeyT().objType() == Apfs.JObjTypes.APFS_TYPE_INODE &&
                            file_entry.val() instanceof Apfs.JInodeValT) {

                        Map<String, Object> item = new HashMap<>();
                        int index = 0;
                        for (XFieldT xf_h : ((Apfs.JInodeValT) file_entry.val()).xfields().xfData()) {
                            if (xf_h.xType() == Apfs.InoExtType.INO_EXT_TYPE_NAME) {
                                item.put("name", ((Apfs.XfName) ((Apfs.JInodeValT) file_entry.val()).xfields().xf().get(index)).name());
                            } else if (xf_h.xType() == Apfs.InoExtType.INO_EXT_TYPE_DSTREAM) {
                                item.put("file_size", ((Apfs.XfSize) ((Apfs.JInodeValT) file_entry.val()).xfields().xf().get(index)).size());
                            }
                            index++;
                        }
                        if (item.get("name") == null) {
                            throw new Exception("name not found");
                        }
                        item.put("node_id", file_entry.jKeyT().objId());
                        item.put("parent", ((Apfs.JInodeValT) file_entry.val()).parentId());
                        item.put("private_id", ((Apfs.JInodeValT) file_entry.val()).privateId());
                        item.put("creationtime", ((Apfs.JInodeValT) file_entry.val()).changeTime());
                        item.put("accesstime", ((Apfs.JInodeValT) file_entry.val()).accessTime());
                        item.put("modificationtime", ((Apfs.JInodeValT) file_entry.val()).modTime());
                        if (((Apfs.JInodeValT) file_entry.val()).xfields().xfNumExts() == 1) {
                            item.put("type", "folder");
                        } else {
                            item.put("type", "file");
                        }

                        itemmap.get(xid).get(volume).put((long) file_entry.jKeyT().objId(), item);

System.err.println("inode: " + item.get("name"));
                    }

                    // } catch (Exception ex) {
                    //     logger.error("File entry %s %s parsing failed %s",
                    //         file_entry.j_key_t.obj_id, file_entry.j_key_t.obj_type, ex);
                    // }
                }
            }
        }

        ItemStore store = new ItemStore();
        for (long xid : file_entries.keySet()) {

            for (String volume : file_entries.get(xid).keySet()) {

                List<Map<String, Object>> s = new ArrayList<>(itemmap.get(xid).get(volume).values());
                Collections.sort(s, (o1, o2) -> (int)((long) o1.get("parent") - (long) o2.get("parent")));
s.forEach(Debug::println);

                for (Map<String, Object> item : s) {

                    // path on image
                    String path = get_path((Map) itemmap.get(xid).get(volume), (long) (int) item.get("node_id"));

                    List<Map<String, Object>> extents = new ArrayList<>();

                    MessageDigest md5 = MessageDigest.getInstance("md5");
                    if (item.get("type").equals("file")) {
                        long remaining = (long) item.get("file_size");

                        Collections.sort(
                             extentmap.get(xid).get(volume).getOrDefault((long) item.get("private_id"), new ArrayList<>()), (o1, o2) -> (int) ((long) o1.get("offset") - (long) o1.get("offset")));
                        extents = extentmap.get(xid).get(volume).getOrDefault((long) item.get("private_id"), new ArrayList<>());

                        for (Map<String, Object> extent : extents) {
                            Map<String, Object> intermediate = process_extent(extent, remaining, blocksize, file_io, md5);
                            md5 = (MessageDigest) intermediate.get("md5");
                            remaining = (long) intermediate.get("remaining");
                        }
                    }

                    store.add_item((String) item.get("type"), xid, (long) item.get("parent"), (long) (int) item.get("node_id"), "exists", volume, Paths.get(path).getParent().toString(),
                                   (String) item.get("name"), ((long) item.get("accesstime")) / 1000000000,
                                   ((long) item.get("modificationtime")) / 1000000000, ((long) item.get("creationtime")) / 1000000000,
                                   item.containsKey("file_size") ? (long) item.get("file_size") : 0,
                                   extents.size() != 0 ? ByteUtil.toHexString(md5.digest()) : "0", extents);
                }
            }
        }

        return store;
    }
}