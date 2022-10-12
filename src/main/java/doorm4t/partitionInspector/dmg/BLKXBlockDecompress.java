/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.dmg;

import java.nio.ByteBuffer;
import java.util.zip.Inflater;


public class BLKXBlockDecompress {
    private static final int SECTOR_SIZE = 512; // not sure if this is the correct sector size.

    public static byte[] decompressBLKXBlock(ByteBuffer dataForkBuffer, doorm4t.partitionInspector.dmg.MishBlock.BLKXChunkEntry block) throws Exception {
        // Don't decompress if the bytes aren't zlib compressed.
        //if (block.EntryType != 0x80000005) // hex code for zlib compression
        //    throw new Exception("Non-zLib compressed bytes with type " + String.format("%08X", block.EntryType) + ". Skipping decompression.");

        byte[] bytes = new byte[(int) block.CompressedLength];
        dataForkBuffer.get(bytes, (int) block.CompressedOffset, (int) block.CompressedLength);
        ByteBuffer compressedChunkBytesBuffer = ByteBuffer.wrap(bytes);
        byte[] compressedBytes = new byte[compressedChunkBytesBuffer.remaining()];
        compressedChunkBytesBuffer.get(compressedBytes);

        Inflater decompresser = new Inflater();
        decompresser.setInput(compressedBytes, 0, compressedBytes.length);
        int decompressedSize = (int) block.SectorCount * SECTOR_SIZE;
        byte[] result = new byte[decompressedSize];
        decompresser.inflate(result);
        decompresser.end();
        return result;
    }
}
