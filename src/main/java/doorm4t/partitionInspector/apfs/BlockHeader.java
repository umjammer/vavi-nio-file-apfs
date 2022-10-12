/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.apfs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import vavi.util.StringUtil;
import vavi.util.serdes.Element;
import vavi.util.serdes.Serdes;


/**
 * obj_phys_t
 *
 * A header used at the beginning of all objects.
 */
@Serdes(bigEndian = false)
public class BlockHeader {

    /** uint8_t[MAX_CKSUM_SIZE=8] o_cksum */
    @Element(sequence = 1)
    public long checksum;
    /** uint64_t o_oid */
    @Element(sequence = 2)
    public long block_id;
    /** uint64_t o_xid */
    @Element(sequence = 3)
    public long version;
    /** uint32_t[low:16] o_type ... Object Types */
    @Element(sequence = 4)
    public short block_type;
    /** uint32_t[high:16] o_type ... Object Type Flags */
    @Element(sequence = 5)
    public short flags;
    /** uint32_t o_subtype */
    @Element(sequence = 6)
    public int padding;

    public BlockHeader() {
    }

    public BlockHeader(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        checksum = buffer.getLong();
        block_id = buffer.getLong();
        version = buffer.getLong();
        block_type = buffer.getShort();
        flags = buffer.getShort();
        padding = buffer.getInt();
    }

    @Override
    public String toString () {
        return String.format("{Checksum: %16x, Block ID: %s, Version: %08x, Block Type: %s, Flags: %s(0x%04x), Padding: %s}",
                checksum, block_id, version, ObjectType.byId(block_type),
                StringUtil.toBits(flags, 16), flags, padding);
    }
}
