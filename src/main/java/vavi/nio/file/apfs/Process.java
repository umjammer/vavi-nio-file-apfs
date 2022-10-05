package vavi.nio.file.apfs;

import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import io.kaitai.struct.KaitaiStream;
import vavi.nio.file.apfs.Apfs.NodeEntry;
import vavi.nio.file.apfs.Apfs.XFieldT;
import vavi.util.ByteUtil;


/**
 * Multiple output functions
 */
public class Process {

    static final Logger logger = Logger.getLogger(Process.class.getName());

    String getPath(Map<Long, Map<String, Object>> itemMap, long fid) {
        if (itemMap.containsKey(fid)) {
            String name = itemMap.get(fid).get("name") != null ? (String) itemMap.get(fid).get("name") : "____" + fid + "____";
            String parent_path = (long) itemMap.get(fid).get("parent") > 1 ? getPath(itemMap, (long) itemMap.get(fid).get("parent")) : "";
            return parent_path + "/" + name;
        }
        return "~~~~" + fid + "~~~~";
    }

    Map<String, Object> processExtent(Map<String, Object> extent, long remaining, int blockSize, KaitaiStream io, MessageDigest md5) {
        for (int blockPart = 0; blockPart < (long) (extent.get("length")) / blockSize; blockPart++) {
            byte[] data = Block.get_block((int) ((long) extent.get("start")) + blockPart, blockSize, io);
            long chunk_size;
            if (remaining < blockSize) {
                chunk_size = remaining;
            } else {
                chunk_size = blockSize;
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
    public ItemStore processEntries(Map<Long, Map<String, List<NodeEntry>>> entries, int blockSize, KaitaiStream io) throws Exception {

        Map<Long, Map<String, Map<Long, List<Map<String, Object>>>>> extentMap = new HashMap<>();
        Map<Long, Map<String, Map<Long, Map<String, Object>>>> itemMap = new HashMap<>();

        for (long xid : entries.keySet()) {

            extentMap.put(xid, new HashMap<>());
            itemMap.put(xid, new HashMap<>());

            for (String volume : entries.get(xid).keySet()) {

                extentMap.get(xid).put(volume, new HashMap<>());
                itemMap.get(xid).put(volume, new HashMap<>());

                for (NodeEntry entry : entries.get(xid).get(volume)) {
//try {
                    if (entry.jKeyT().objType() == Apfs.JObjTypes.APFS_TYPE_FILE_EXTENT &&
                            entry.val() instanceof Apfs.JExtentValT) {

                        extentMap.get(xid).get(volume).computeIfAbsent((long) entry.jKeyT().objId(), k -> new ArrayList<>());
                        Map<String, Object> m = new HashMap<String, Object>() {{
                            put("start", ((Apfs.JExtentValT) entry.val()).physBlockNum());
                            put("length", ((Apfs.JExtentValT) entry.val()).len());
                            put("offset", ((Apfs.JExtentKeyT) entry.key()).offset());
                        }};
                        extentMap.get(xid).get(volume).get((long) entry.jKeyT().objId()).add(m);

logger.fine("extent: " + entry.jKeyT().objId() + ", " + xid);

                    } else if (entry.jKeyT().objType() == Apfs.JObjTypes.APFS_TYPE_INODE &&
                            entry.val() instanceof Apfs.JInodeValT) {

                        Map<String, Object> item = new HashMap<>();
                        int index = 0;
                        for (XFieldT xf_h : ((Apfs.JInodeValT) entry.val()).xfields().xfData()) {
                            if (xf_h.xType() == Apfs.InoExtType.INO_EXT_TYPE_NAME) {
                                item.put("name", ((Apfs.XfName) ((Apfs.JInodeValT) entry.val()).xfields().xf().get(index)).name());
                            } else if (xf_h.xType() == Apfs.InoExtType.INO_EXT_TYPE_DSTREAM) {
                                item.put("file_size", ((Apfs.XfSize) ((Apfs.JInodeValT) entry.val()).xfields().xf().get(index)).size());
                            }
                            index++;
                        }
                        if (item.get("name") == null) {
                            throw new Exception("name not found");
                        }
                        item.put("node_id", entry.jKeyT().objId());
                        item.put("parent", ((Apfs.JInodeValT) entry.val()).parentId());
                        item.put("private_id", ((Apfs.JInodeValT) entry.val()).privateId());
                        item.put("creationtime", ((Apfs.JInodeValT) entry.val()).changeTime());
                        item.put("accesstime", ((Apfs.JInodeValT) entry.val()).accessTime());
                        item.put("modificationtime", ((Apfs.JInodeValT) entry.val()).modTime());
                        if (((Apfs.JInodeValT) entry.val()).xfields().xfNumExts() == 1) {
                            item.put("type", "folder");
                        } else {
                            item.put("type", "file");
                        }

                        itemMap.get(xid).get(volume).put((long) entry.jKeyT().objId(), item);

logger.fine("inode: " + item.get("name"));
                    }

//} catch (Exception ex) {
// logger.severe(String.format("File entry %s, %s parsing failed %s",
//  entry.jKeyT().objId(), entry.jKeyT().objType(), ex));
// ex.printStackTrace();
//}
                }
            }
        }

        ItemStore store = new ItemStore();
        for (long xid : entries.keySet()) {

            for (String volume : entries.get(xid).keySet()) {

                List<Map<String, Object>> s = new ArrayList<>(itemMap.get(xid).get(volume).values());
                s.sort((o1, o2) -> (int) ((long) o1.get("parent") - (long) o2.get("parent")));
//s.forEach(Debug::println);

                for (Map<String, Object> item : s) {

                    // path on image
                    String path = getPath(itemMap.get(xid).get(volume), (int) item.get("node_id"));

                    List<Map<String, Object>> extents = new ArrayList<>();

                    MessageDigest md5 = MessageDigest.getInstance("md5");
                    if (item.get("type").equals("file")) {
                        long remaining = (long) item.get("file_size");

                        extentMap.get(xid).get(volume).getOrDefault((long) item.get("private_id"),
                                new ArrayList<>()).sort((o1, o2) -> (int) ((long) o1.get("offset") - (long) o2.get("offset")));
                        extents = extentMap.get(xid).get(volume).getOrDefault((long) item.get("private_id"), new ArrayList<>());

                        for (Map<String, Object> extent : extents) {
                            Map<String, Object> intermediate = processExtent(extent, remaining, blockSize, io, md5);
                            md5 = (MessageDigest) intermediate.get("md5");
                            remaining = (long) intermediate.get("remaining");
                        }
                    }

                    store.addItem((String) item.get("type"), xid, (long) item.get("parent"), (int) item.get("node_id"), "exists", volume, Paths.get(path).getParent().toString(),
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