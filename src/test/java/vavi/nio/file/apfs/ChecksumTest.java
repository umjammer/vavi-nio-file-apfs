/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.nio.file.apfs;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import vavi.util.ByteUtil;
import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * ChecksumTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/12/01 umjammer initial version <br>
 */
class ChecksumTest {

    @Test
    void test() throws Exception {
        byte[] data = Files.readAllBytes(Paths.get("src/test/resources/test.blk"));
Debug.printf("%016x, %016x", ByteUtil.readLeLong(data, 0), Checksum.create_checksum(Arrays.copyOfRange(data, 8, data.length)));
        assertEquals(ByteUtil.readLeLong(data, 0), Checksum.create_checksum(Arrays.copyOfRange(data, 8, data.length)));
    }
}

/* */
