
package vavi.nio.file.apfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import vavi.nio.file.apfs.Apfs.ApfsSuperblockT;
import vavi.nio.file.apfs.Apfs.BtreeNodePhysT;
import vavi.nio.file.apfs.Apfs.NodeEntry;
import vavi.nio.file.apfs.Apfs.NxSuperblockT;
import vavi.nio.file.apfs.Apfs.Obj;
import vavi.nio.file.apfs.Apfs.OmapValT;

import io.kaitai.struct.KaitaiStream;
import vavi.util.Debug;


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

    static final Logger logger = Logger.getLogger(Parse.class.getName());

    /**
     * @param volumeOverride nullable
     */
    static Map<Long, Map<String, List<NodeEntry>>> addEntries(Map<Long, Map<String, List<NodeEntry>>> entries,
                                                              Map<Long, Map<String, List<NodeEntry>>> newEntries,
                                                              long xidOverride,
                                                              String volumeOverride) {
        for (long xid : newEntries.keySet()) {
            entries.computeIfAbsent(xidOverride, k -> new HashMap<>());
            for (String volume : newEntries.get(xid).keySet()) {
                volumeOverride = volumeOverride != null ? volumeOverride : volume;
                entries.get(xidOverride).computeIfAbsent(volumeOverride, k -> new ArrayList<>());
                entries.get(xidOverride).get(volumeOverride).addAll(newEntries.get(xid).get(volume));
            }
        }
        return entries;
    }

    /**
     * 'unknown' is the default volume name node_type 1 contains only pointer
     * records
     */
    static Map<Long, Map<String, List<NodeEntry>>> parseNode(Obj node, KaitaiStream io) {
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

    static Map<Long, Map<String, List<NodeEntry>>> parseSpsb(Obj apsb, KaitaiStream io) {
        Map<Long, Map<String, List<NodeEntry>>> entries = new HashMap<>();

        for (NodeEntry oMapEntry : Low.get_apsb_objects(apsb)) {
            // get root directory
            Obj rootNode = ((OmapValT) oMapEntry.val()).ovPaddr().target();
            Map<Long, Map<String, List<NodeEntry>>> newEntries = parseNode(rootNode, io);
            entries = addEntries(entries,
                    newEntries,
                    apsb.hdr().oXid().val(),
                    ((ApfsSuperblockT) apsb.body()).apfsVolname());
        }
        return entries;
    }

    static Map<Long, Map<String, List<NodeEntry>>> parseNxsb(Obj nxsb, KaitaiStream io) {
        Map<Long, Map<String, List<NodeEntry>>> entries = new HashMap<>();
//Debug.printf("parse_nxsb: %s\n", nxsb);

//Debug.printf("get_nxsb_objects: %s\n", Low.get_nxsb_objects(nxsb));
        for (NodeEntry entry : Low.get_nxsb_objects(nxsb)) {
            // get volume superblock
Debug.printf("entry: %s", entry);
try {
            Obj apsb = ((OmapValT) entry.val()).ovPaddr().target();
Debug.printf("apsb: %s\n", nxsb);
            Map<Long, Map<String, List<NodeEntry>>> newEntries = parseSpsb(apsb, io);
            entries = addEntries(entries, newEntries, nxsb.hdr().oXid().val(), null);
} catch (Exception e) {
 Debug.println(e);
 e.printStackTrace();
}
        }

        return entries;
    }

    /** parse image and print files */
    public static Map<Long, Map<String, List<NodeEntry>>> parse(KaitaiStream io, Apfs apfs, int blockSize) throws IOException {

        // get from container superblock
        Obj nxsb = apfs.block0();
        Map<Long, Map<String, List<NodeEntry>>> entries = parseNxsb(nxsb, io);
        long prevNxsb = ((NxSuperblockT) nxsb.body()).nxXpDescBase() + ((NxSuperblockT) nxsb.body()).nxXpDescIndex() + 1;
        long count = ((NxSuperblockT) nxsb.body()).nxXpDescLen();

        // get from older container superblocks
        for (int i = 0; i < count; i++) {
            byte[] data = Block.get_block((int) prevNxsb, blockSize, io);
            nxsb = new Obj(new ByteBufferKaitaiStream2(data), apfs, apfs);
            try {
                entries.putAll(parseNxsb(nxsb, io));
                prevNxsb = ((NxSuperblockT) nxsb.body()).nxXpDescBase() + ((NxSuperblockT) nxsb.body()).nxXpDescIndex() + 1;
            } catch (Exception e) {
                break;
            }
        }

        return entries;
    }
}
