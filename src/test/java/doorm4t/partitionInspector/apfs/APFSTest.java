/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package doorm4t.partitionInspector.apfs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.logging.Level;

import doorm4t.partitionInspector.apfs.bTreeNode.BTreeNode;
import doorm4t.partitionInspector.apfs.kv.keys.OMAPKey;
import doorm4t.partitionInspector.apfs.kv.values.OMAPValue;
import doorm4t.partitionInspector.utils.Utils;
import vavi.util.Debug;
import vavi.util.serdes.Serdes;


/**
 * APFSTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/10/09 nsano initial version <br>
 */
class APFSTest {

    static class MySeekableByteChannel implements SeekableByteChannel {
        SeekableByteChannel sbc;
        long offset;
        MySeekableByteChannel(SeekableByteChannel sbc, long offset) throws IOException {
            this.sbc = sbc;
            this.offset = offset;
            sbc.position(offset);
        }
        @Override public int read(ByteBuffer dst) throws IOException {
            return sbc.read(dst);
        }
        @Override public int write(ByteBuffer src) throws IOException {
            return sbc.write(src);
        }
        @Override public long position() throws IOException {
            return sbc.position() - offset; // TODO
        }
        @Override public SeekableByteChannel position(long newPosition) throws IOException {
Debug.printf(Level.FINER, "position: %016x / %016x", offset + newPosition, sbc.size());
            assert offset + newPosition > 0 && offset + newPosition < sbc.size();
            return sbc.position(offset + newPosition);
        }
        @Override public long size() throws IOException {
            return sbc.size();
        }
        @Override public SeekableByteChannel truncate(long size) throws IOException {
            return sbc.truncate(size);
        }
        @Override public boolean isOpen() {
            return sbc.isOpen();
        }
        @Override public void close() throws IOException {
            sbc.close();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        APFSTest app = new APFSTest();
        app.test1();
    }

    void test2() throws Exception {
        Utils.OFFSET = 409640 * 512;
        APFS apfs = new APFS("/Users/nsano/work/fusion/img/nsanomac4_ssd.img");
    }

    void test1() throws Exception {
        long offset = 409640 * 512;
        Path apfs = Paths.get("/Users/nsano/work/fusion/img/nsanomac4_ssd.img");

        SeekableByteChannel sbc = new MySeekableByteChannel(Files.newByteChannel(apfs), offset);

        BlockHeader header = new BlockHeader();
        Serdes.Util.deserialize(sbc, header);

        ApfsContainer container = new ApfsContainer();
        Serdes.Util.deserialize(sbc, container);
        container.setHeader(header);
        assert Arrays.equals("NXSB".getBytes(), container.magic);
Debug.println(container);

        int blockSize = container.nx_block_size;
Debug.printf("block * blocks: %016x, size: %016x", container.nx_block_count * blockSize, sbc.size());


        long csbOmapOffset = container.nx_omap_oid * blockSize;
        sbc.position(csbOmapOffset);

        header = new BlockHeader();
        Serdes.Util.deserialize(sbc, header);

        OMap csbOMap = new OMap();
        Serdes.Util.deserialize(sbc, csbOMap);
        csbOMap.setHeader(header);
Debug.println(csbOMap);
        container.csbOMap = csbOMap;





//        for (long volOID : container.nx_fs_oid) {
//            int vsbOffset = container.csbOMap.parsedOmap.get(volOID).intValue() * blockSize;
//            sbc.position(vsbOffset);
//
//            header = new BlockHeader();
//            Serdes.Util.deserialize(sbc, header);
//
//            ApfsVolume volume = new ApfsVolume();
//            Serdes.Util.deserialize(sbc, volume);
//            container.setHeader(header);
//Debug.println(volume);
//        }







        for (long i = 1; i * blockSize < sbc.size(); i++) {
            long blockOffset = i * blockSize;

            sbc.position(blockOffset);

            header = new BlockHeader();
            Serdes.Util.deserialize(sbc, header);

            if (header.block_type == 2 || header.block_type == 3) {
Debug.printf("block[%d]: %016x, %d, ... %016x", i, header.block_id, header.block_type, container.nx_omap_oid);
                if (container.nx_omap_oid == header.block_id) {
Debug.printf("BINGO!!!!!!!!!!!!!!!!!! block[%d]: %016x, %d", i, header.block_id, header.block_type);
                    break;
                }
            }
        }
    }

    void parseOmapBTree(OMap csbOMap, long csbOmapOffset, SeekableByteChannel sbc, int blockSize) throws IOException {
        // Parse the OMAP's root node
        BTreeNode rootNode;
        if (csbOMap.om_o.block_type != 2 && csbOMap.om_o.block_type != 3) {
            long csbOMAPBTreeOffset = csbOMap.om_tree_oid * blockSize;
            sbc.position(csbOMAPBTreeOffset);
            rootNode = new BTreeNode();
            Serdes.Util.deserialize(sbc, rootNode);
        } else {
            sbc.position(csbOmapOffset);
            rootNode = new BTreeNode();
            Serdes.Util.deserialize(sbc, rootNode);
        }
Debug.println(rootNode);

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
                    csbOMap.parsedOmap.put(key.ok_oid, val.paddr_t);
                }
            } else {
                for (OMAPValue omapVal : n.omapValues) {
Debug.println(Level.FINER, "CHILD NODE AT BLOCK " + omapVal.paddr_t);
                    long physOffset = omapVal.paddr_t * blockSize;
                    sbc.position(physOffset);
                    BTreeNode node = new BTreeNode();
                    Serdes.Util.deserialize(sbc, rootNode);
                    nodes.add(node);
                }
            }
        }
    }
}