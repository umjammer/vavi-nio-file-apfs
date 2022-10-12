/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.apfs;

import doorm4t.partitionInspector.apfs.bTreeNode.BTreeNode;
import doorm4t.partitionInspector.apfs.kv.keys.OMAPKey;
import doorm4t.partitionInspector.apfs.kv.values.OMAPValue;
import doorm4t.partitionInspector.utils.Utils;
import vavi.util.Debug;
import vavi.util.serdes.Element;
import vavi.util.serdes.Serdes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.logging.Level;


// See APFS Reference pg. 44
@Serdes(bigEndian = false)
public class OMap implements ApfsObject {

    BlockHeader om_o;

    @Element(sequence = 1)
    int om_flags;
    @Element(sequence = 2)
    int om_snap_count;
    @Element(sequence = 3)
    int om_tree_type;
    @Element(sequence = 4)
    int om_snapshot_tree_type;
    @Element(sequence = 5)
    long om_tree_oid;
    @Element(sequence = 6)
    long om_snapshot_tree_oid;
    @Element(sequence = 7)
    long om_most_recent_snap;
    @Element(sequence = 8)
    long om_pending_revert_min;

    // OID -> Physical Address
    HashMap<Long, Long> parsedOmap = new HashMap<>();

    public OMap() {}

    public OMap(ByteBuffer buffer, String imagePath, int blockSize) throws IOException {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        om_o = new BlockHeader(buffer);

        om_flags = buffer.getInt();
        om_snap_count = buffer.getInt();
        om_tree_type = buffer.getInt();
        om_snapshot_tree_type = buffer.getInt();

        om_tree_oid = buffer.getLong();
        om_snapshot_tree_oid = buffer.getLong();
        om_most_recent_snap = buffer.getLong();
        om_pending_revert_min = buffer.getLong();

        parseOmapBTree(imagePath, blockSize);
    }

    /**
     * Parses the OMAP's BTree, mapping oids to OMAP Values in a hash map
     *
     * @param imagePath
     * @param blockSize
     * @throws IOException
     */
    private void parseOmapBTree(String imagePath, int blockSize) throws IOException {
        // Parse the OMAP's root node
        int csbOMAPBTreeOffset = (int) om_tree_oid * blockSize;
        ByteBuffer rootNodeBuffer = Utils.GetBuffer(imagePath, csbOMAPBTreeOffset, blockSize);
        BTreeNode rootNode = new BTreeNode(rootNodeBuffer);

        ArrayDeque<BTreeNode> nodes = new ArrayDeque<>();
        nodes.add(rootNode);
        while (nodes.size() > 0) {
            BTreeNode n = nodes.removeFirst();
Debug.println(Level.FINER, n);
            if (n.btn_flags_is_leaf) {
                for (int i = 0; i < n.omapKeys.size(); i++) {
                    OMAPKey key = n.omapKeys.get(i);
                    OMAPValue val = n.omapValues.get(i);
Debug.println(Level.FINER, "LEAF ENTRY KEY " + key.ok_oid);
Debug.println(Level.FINER, "LEAF ENTRY VAL " + val);
                    parsedOmap.put(key.ok_oid, val.paddr_t);
                }
            } else {
                for (OMAPValue omapVal : n.omapValues) {
Debug.println(Level.FINER, "CHILD NODE AT BLOCK " + omapVal.paddr_t);
                    int physOffset = (int) omapVal.paddr_t * blockSize;
                    ByteBuffer childNodeBytes = Utils.GetBuffer(imagePath, physOffset, blockSize);
                    nodes.add(new BTreeNode(childNodeBytes));
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("\n\tOMAP Header %s\n\tFlags: %s, Snap Count: %s, Tree type: %08x, Snapshot Tree Type: %08x, Tree OID: %016x, Snapshot Tree OID: %016x, Most Recent Snap: %s\n\tParsed OMAP: %s", om_o, om_flags, om_snap_count, om_tree_type, om_snapshot_tree_type, om_tree_oid, om_snapshot_tree_oid, om_most_recent_snap, parsedOmap.toString());
    }

    @Override
    public BlockHeader getHeader() {
        return om_o;
    }

    @Override
    public void setHeader(BlockHeader header) {
        om_o = header;
    }
}
