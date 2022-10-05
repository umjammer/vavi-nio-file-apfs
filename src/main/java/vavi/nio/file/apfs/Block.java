package vavi.nio.file.apfs;

import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import io.kaitai.struct.KaitaiStream;


class Block {

    /** Get data of a single block */
    static byte[] get_block(int idx, int block_size, KaitaiStream file_io) {
        file_io.seek((long) idx * block_size);
        return file_io.readBytes(block_size);
    }

    static long checksum(byte[] b) {
        Checksum checksum = new CRC32();
        checksum.update(b, 0, b.length);
        return checksum.getValue();
    }
}