/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import vavi.nio.file.apfs.ByteBufferKaitaiStream2;
import vavi.nio.file.apfs.Carve;
import vavi.nio.file.apfs.ItemStore;
import vavi.nio.file.apfs.Parse;
import vavi.nio.file.apfs.Process;
import vavi.nio.file.apfs.Apfs;
import vavi.nio.file.apfs.Apfs.NodeEntry;
import vavi.nio.file.apfs.Apfs.NxSuperblockT;
import vavi.nio.file.apfs.Apfs.Obj;

import io.kaitai.struct.KaitaiStream;
import vavi.util.Debug;


/**
 * Afro.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/17 umjammer initial version <br>
 */
public class Afro {

    @Test
    void test0() throws Exception {
        main(new String[] {"/Users/nsano/src/python/afro/test/wsdf.dmg", "40", "parse"});
    }

    @Test
    void test1() throws Exception {
        main(new String[] {"/Users/nsano/work/fusion/img/nsanomac4_ssd.img", "409640", "carve-nodes"});
    }

    /**
     * @param args 0: filename, 1: offset
     */
    public static void main(String[] args) throws Exception {
        String fileName = args[0];
        long offset = Long.parseLong(args[1]) * 512;
        String command = args[2];

        KaitaiStream io = new ByteBufferKaitaiStream2(fileName, offset);
        Apfs apfs = new Apfs(io);

        Obj nxsb = apfs.block0();
        int blockSize = (int) ((NxSuperblockT) nxsb.body()).nxBlockSize();
//Debug.printf("blockSize: %d", blockSize);
        io.seek(0L);

        Map<Long, Map<String, List<NodeEntry>>> es;
Debug.printf("command: %s", command);
        switch (command) {
        case "parse":
        default:
            es = Parse.parse(io, apfs, blockSize);
            break;
        case "carve-nxsb":
            es = new Carve().nxsb(io, apfs, blockSize);
            break;
        case "carve-apsb":
            es = new Carve().apsb(io, apfs, blockSize);
            break;
        case "carve-nodes":
            es = new Carve().nodes(io, apfs, blockSize);
            break;
        }
        ItemStore store = new Process().processEntries(es, blockSize, io);
        String basename = Paths.get(fileName).getFileName().toString();
        store.saveFiles("tmp/" + basename + ".extracted", blockSize, io);
        store.saveBodyFile("tmp/" + basename + ".bodyfile");
        store.save_gtf("tmp/" + basename + ".gtf");
    }
}

/* */
