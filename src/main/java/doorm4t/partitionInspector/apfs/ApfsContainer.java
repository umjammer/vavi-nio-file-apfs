/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.apfs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import doorm4t.partitionInspector.utils.Utils;
import vavi.util.serdes.Element;
import vavi.util.serdes.Serdes;


// See APFS reference page 26 (Container Superblock)
@Serdes(bigEndian = false)
public class ApfsContainer implements ApfsObject {

    public BlockHeader blockHeader;

    @Element(sequence = 1)
    public byte[] magic = new byte[4];
    @Element(sequence = 2)
    public int nx_block_size;
    @Element(sequence = 3)
    public long nx_block_count;

    @Element(sequence = 4)
    public long nx_features;
    @Element(sequence = 5)
    public long nx_read_only_compatible_features;
    @Element(sequence = 6)
    public long nx_incompatable_features;

    @Element(sequence = 7)
    public byte[] nx_uuid = new byte[16];

    @Element(sequence = 8)
    public long nx_next_oid;
    @Element(sequence = 9)
    public long nx_next_xid;

    @Element(sequence = 10)
    public int nx_xp_desc_blocks;
    @Element(sequence = 11)
    public int nx_xp_data_blocks;
    @Element(sequence = 12)
    public long nx_xp_desc_base;
    @Element(sequence = 13)
    public long nx_xp_data_base;
    @Element(sequence = 14)
    public int nx_xp_desc_next;
    @Element(sequence = 15)
    public int nx_xp_data_next;
    @Element(sequence = 16)
    public int nx_xp_desc_index;
    @Element(sequence = 17)
    public int nx_xp_desc;
    @Element(sequence = 18)
    public int nx_xp_data_index;
    @Element(sequence = 19)
    public int nx_xp_data;

    @Element(sequence = 20)
    public long nx_spaceman_oid;
    @Element(sequence = 21)
    public long nx_omap_oid; // Physical object identifier (physical address) of the CSB OMAP
    @Element(sequence = 22)
    public long nx_reaper_oid;

    @Element(sequence = 23)
    public int nx_test_type;

    @Element(sequence = 24)
    public int nx_max_file_systems;
    @Element(sequence = 25, value = "$24")
    public long[] nx_fs_oid;

    public OMap csbOMap;

    public ApfsContainer() {
    }

    public ApfsContainer(ByteBuffer buffer, String imagePath) throws IOException {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] bytes = new byte[32];
        buffer.get(bytes, 0, 32);
        blockHeader = new BlockHeader(ByteBuffer.wrap(bytes));
        buffer.position(32);
        buffer.get(magic);
        nx_block_size = buffer.getInt();
        nx_block_count = buffer.getLong();
        nx_features = buffer.getLong();
        nx_read_only_compatible_features = buffer.getLong();
        nx_incompatable_features = buffer.getLong();
        buffer.get(nx_uuid);
        nx_next_oid = buffer.getLong();
        nx_next_xid = buffer.getLong();
        nx_xp_desc_blocks = buffer.getInt();
        nx_xp_data_blocks = buffer.getInt();
        nx_xp_desc_base = buffer.getLong();
        nx_xp_data_base = buffer.getLong();
        nx_xp_desc_next = buffer.getInt();
        nx_xp_data_next = buffer.getInt();
        nx_xp_desc_index = buffer.getInt();
        nx_xp_desc = buffer.getInt();
        nx_xp_data_index = buffer.getInt();
        nx_xp_data = buffer.getInt();
        nx_spaceman_oid = buffer.getLong();
        nx_omap_oid = buffer.getLong();
        nx_reaper_oid = buffer.getLong();
        nx_test_type = buffer.getInt();
        nx_max_file_systems = buffer.getInt();

        nx_fs_oid = new long[nx_max_file_systems];
        for (int i = 0; i < nx_max_file_systems; i++) {
            nx_fs_oid[i] = buffer.getLong();
        }

        // Get the CSB Object Map (OMAP), which maps OIDs to values containing a physical address
        // nx_omap_oid is a physical address
        int csbOmapOffset = (int) nx_omap_oid * nx_block_size;
        ByteBuffer csbOMapBuffer = Utils.GetBuffer(imagePath, csbOmapOffset, nx_block_size);
        csbOMap = new OMap(csbOMapBuffer, imagePath, nx_block_size);
    }

    public static ApfsContainer parseContainer(String path) throws IOException {
        ByteBuffer buffer = Utils.GetBuffer(path, 0, 512);
        ApfsContainer block = new ApfsContainer(buffer, path);
        return block;
    }

    @Override
    public String toString() {
        return String.format("APFS Super Block\nBlock Header " +
                "%s\nMagic: %s\nBlock Size: %s\nBlock Count: %s\nSpaceman OID: %s\nOMAP OID: %s\nReaper OID: %s\n",
                blockHeader, new String(magic), nx_block_size, nx_block_count, nx_spaceman_oid, nx_omap_oid, nx_reaper_oid);
    }

    @Override
    public BlockHeader getHeader() {
        return blockHeader;
    }

    @Override
    public void setHeader(BlockHeader header) {
        blockHeader = header;
    }
}
