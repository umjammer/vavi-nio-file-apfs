
package vavi.nio.file.apfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import vavi.nio.file.apfs.Apfs.NodeEntry;
import vavi.nio.file.apfs.Apfs.Obj;
import vavi.util.ByteUtil;

import io.kaitai.struct.KaitaiStream;
import vavi.util.Debug;


/**
 * carve file system
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/16 umjammer initial version <br>
 */
public class Carve {

    static final Logger logger = Logger.getLogger(Carve.class.getName());

    /**
     * Carve NXSB: - Iterate all blocks and search for magic bytes -> parse from 1.
     */
    public Map<Long, Map<String, List<NodeEntry>>> nxsb(KaitaiStream image_io, Apfs apfs, int blockSize) throws IOException {
Debug.printf("carve::nxsb");
        return carve(image_io, apfs, blockSize, "nxsb", match_magic_func("NXSB"), Parse::parseNxsb);
    }

    /**
     * Carve APSB: - Iterate all block and search for magic bytes -> parse from 6.
     */
    public Map<Long, Map<String, List<NodeEntry>>> apsb(KaitaiStream image_io, Apfs apfs, int blockSize) throws IOException {
Debug.printf("carve::apsb");
        return carve(image_io, apfs, blockSize, "apsb", match_magic_func("APSB"), Parse::parseSpsb);
    }

    /**
     * Carve nodes: - Iterate all block and search for patterns -> parse from 9.
     */
    public Map<Long, Map<String, List<NodeEntry>>> nodes(KaitaiStream image_io, Apfs apfs, int blockSize) throws IOException {
Debug.printf("carve::nodes");
        return carve(image_io, apfs, blockSize, "nodes", this::match_nodes, Parse::parseNode);
    }

    Function<byte[], Boolean> match_magic_func(String magic) {
        return data -> ByteUtil.readLeInt(data, 32) == ByteUtil.readLeInt(magic.getBytes()) && Checksum.check_checksum(data);
    }

    boolean match_nodes(byte[] data) {
        int obj_type = ByteUtil.readLeShort(data, 24);
        int subtype = ByteUtil.readLeShort(data, 28);

//Debug.printf("%d, %d, %s\n", obj_type, subtype, Checksum.check_checksum(data));
        return (obj_type == 2 || obj_type == 3) && subtype == 14 && Checksum.check_checksum(data);
    }

    /** parse image and print files */
    Map<Long, Map<String, List<NodeEntry>>> carve(KaitaiStream io,
                                                  Apfs apfs,
                                                  int blockSize,
                                                  String name,
                                                  Function<byte[], Boolean> magic,
                                                  BiFunction<Obj, KaitaiStream, Map<Long, Map<String, List<NodeEntry>>>> get_file_entries_func) throws IOException {

        // get file entries
        Map<Long, Map<String, List<NodeEntry>>> entries = new HashMap<>();
        int i = 0;
int _CC = 0;
        while (true) {
            byte[] data = Block.get_block(i, blockSize, io);
            if (data == null) {
                break;
            } else if (magic.apply(data)) {
logger.info(String.format("Found %s in block %d", name, i));
                try {
                    Obj obj = new Obj(new ByteBufferKaitaiStream2(data), null, apfs);
                    Map<Long, Map<String, List<NodeEntry>>> carved_file_entries = get_file_entries_func.apply(obj, io);
                    for (long xid : carved_file_entries.keySet()) {
                        entries.computeIfAbsent(xid, k -> new HashMap<>());
                        for (String volume : carved_file_entries.get(xid).keySet()) {
                            entries.get(xid).computeIfAbsent(volume, k -> new ArrayList<>());
                            entries.get(xid).get(volume).addAll(carved_file_entries.get(xid).get(volume));
                        }
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                }
_CC += 1;
//if (_CC == 10) {
// Debug.println("force break: count: " + _CC);
// break;
//}
            }

            i += 1;
        }

logger.log(Level.FINE, String.valueOf(entries));
        return entries;
    }
}
