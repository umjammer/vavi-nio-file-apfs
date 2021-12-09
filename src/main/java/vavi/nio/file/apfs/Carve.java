
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

import vavi.nio.file.apfs.kaitai.Apfs.NodeEntry;
import vavi.nio.file.apfs.kaitai.Apfs.Obj;
import vavi.util.ByteUtil;

import io.kaitai.struct.KaitaiStream;


/**
 * carve file system
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/16 umjammer initial version <br>
 */
public class Carve {

    Logger logger = Logger.getLogger(Carve.class.getName());

    /**
     * Carve NXSB: - Iterate all blocks and search for magic bytes -> parse from 1.
     */
    public Map<Long, HashMap<String, List<NodeEntry>>> nxsb(KaitaiStream image_io, int blocksize) throws IOException {
        return carve(image_io, blocksize, "nxsb", match_magic_func("NXSB"), Parse::parse_nxsb);
    }

    /**
     * Carve APSB: - Iterate all block and search for magic bytes -> parse from 6.
     */
    public Map<Long, HashMap<String, List<NodeEntry>>> apsb(KaitaiStream image_io, int blocksize) throws IOException {
        return carve(image_io, blocksize, "apsb", match_magic_func("APSB"), Parse::parse_apsb);
    }

    /**
     * Carve nodes: - Iterate all block and search for patterns -> parse from 9.
     */
    public Map<Long, HashMap<String, List<NodeEntry>>> nodes(KaitaiStream image_io, int blocksize) throws IOException {
        return carve(image_io, blocksize, "nodes", this::match_nodes, Parse::parse_node);
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
    Map<Long, HashMap<String, List<NodeEntry>>> carve(KaitaiStream image_io,
                                                  int blocksize,
                                                  String name,
                                                  Function<byte[], Boolean> magic,
                                                  BiFunction<Obj, KaitaiStream, Map<Long, Map<String, List<NodeEntry>>>> get_file_entries_func) throws IOException {

        // get file entries
        Map<Long, HashMap<String, List<NodeEntry>>> file_entries = new HashMap<>();
        int i = 0;
int _CC = 0;
        while (true) {
            byte[] data = Block.get_block(i, blocksize, image_io);
            if (data == null) {
                break;
            } else if (magic.apply(data)) {
logger.info(String.format("Found %s in block %d", name, i));
                try {
                    Obj obj = new Obj(new ByteBufferKaitaiStream2(data));
                    Map<Long, Map<String, List<NodeEntry>>> carved_file_entries = get_file_entries_func.apply(obj, image_io);
                    for (long xid : carved_file_entries.keySet()) {
                        if (file_entries.get(xid) == null) {
                            file_entries.put(xid, new HashMap<>());
                        }
                        for (String volume : carved_file_entries.get(xid).keySet()) {
                            if (file_entries.get(xid).get(volume) == null) {
                                file_entries.get(xid).put(volume, new ArrayList<>());
                            }
                            file_entries.get(xid).get(volume).addAll(carved_file_entries.get(xid).get(volume));
                        }
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                }
_CC += 1;
//if (_CC == 10)
// break;
            }

            i += 1;
        }

logger.log(Level.FINE, String.valueOf(file_entries));
        return file_entries;
    }
}
