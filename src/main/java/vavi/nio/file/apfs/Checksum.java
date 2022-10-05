package vavi.nio.file.apfs;

import java.util.Arrays;

import vavi.util.ByteUtil;


class Checksum {

    static long create_checksum(byte[] data) {
        long sum1 = 0;
        long sum2 = 0;

        final long mod_value = 0xffff_ffffL;  // 2<<31 - 1;

//Debug.printf("loop : %d\n", data.length / 4);
        for (int i = 0; i < data.length / 4; i++) {
            long value = ByteUtil.readLeInt(data, i * 4) & mod_value;
//Debug.printf("value : %016x\n", value);
            sum1 = ((sum1 + value) % mod_value) & mod_value;
//Debug.printf("sum1  : %016x\n", sum1);
            sum2 = ((sum2 + sum1) % mod_value) & mod_value;
//Debug.printf("sum2  : %016x\n", sum2);
        }

        long check1 = mod_value - ((sum1 + sum2) % mod_value);
//Debug.printf("check1: %016x\n", check1);
        long check2 = mod_value - ((sum1 + check1) % mod_value);
//Debug.printf("check2: %016x\n", check2);

        return (check2 << 32) | check1;
    }

    static boolean check_checksum(byte[] data) {
//Debug.printf("%016x, %016x", ByteUtil.readLeLong(data, 0), Checksum.create_checksum(Arrays.copyOfRange(data, 8, data.length)));
        return ByteUtil.readLeLong(data, 0) == Checksum.create_checksum(Arrays.copyOfRange(data, 8, data.length));
    }
}