/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import vavi.nio.file.apfs.ByteBufferKaitaiStream2;
import vavi.nio.file.apfs.ItemStore;
import vavi.nio.file.apfs.Parse;
import vavi.nio.file.apfs.Process;
import vavi.nio.file.apfs.kaitai.Apfs;
import vavi.nio.file.apfs.kaitai.Apfs.NodeEntry;
import vavi.nio.file.apfs.kaitai.Apfs.NxSuperblockT;
import vavi.nio.file.apfs.kaitai.Apfs.Obj;

import io.kaitai.struct.KaitaiStream;


/**
 * Afro.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/17 umjammer initial version <br>
 */
public class Afro {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String fileName = "/Users/nsano/work/fusion/img/nsanomac4_ssd.img";
        long offset = 409640 * 512;
//        String fileName = "/Users/nsano/src/python/afro/test/wsdf.dmg";
//        long offset = 40 * 512;

        KaitaiStream io = new ByteBufferKaitaiStream2(fileName, offset);
        Apfs apfs = new Apfs(io);

        Obj nxsb = apfs.block0();
        long block_size = ((NxSuperblockT) nxsb.body()).nxBlockSize();
//System.err.printf("block_size: %d\n", block_size);
        io.seek(0l);

        Map<Long, Map<String, List<NodeEntry>>> es = Parse.parse(io);
        ItemStore store = new Process().process_file_entries(es, apfs, (int) block_size, io);
        String basename = Paths.get(fileName).getFileName().toString();
        store.save_files("tmp/" + basename + ".extracted", (int) block_size, io);
        store.save_bodyfile("tmp/" + basename + ".bodyfile");
        store.save_gtf("tmp/" + basename + ".gtf");

//        Carve carve = new Carve();
//        carve.nodes(io, (int) block_size);
    }
}

/* */
