package vavi.nio.file.apfs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import vavi.util.Debug;

import io.kaitai.struct.KaitaiStream;


public class ItemStore {
    private static Logger logger = Logger.getLogger(ItemStore.class.getName());

    List<Map<String, Object>> items;
    Set<Map<String, Object>> seen;

    ItemStore() {
        this.items = new ArrayList<>();
        this.seen = new HashSet<>();
    }

    void reset() {
        this.items.clear();
        this.seen.clear();
    }

    void add_item(String item_type,
                 long xid,
                 long parent_id,
                 long item_id,
                 String status,
                 String volume,
                 String path,
                 String name,
                 long atime,
                 long mtime,
                 long ctime,
                 long size,
                 String md5,
                 List<Map<String, Object>> extents/*=null*/) {
        if (extents == null) {
            extents = new ArrayList<>();
        }
        if (status.equals("exists") && (name == null || name.isEmpty())) {
            System.err.printf("name not found (%d, %d, %d)\n", xid, parent_id, item_id);
        }
        List<Map<String, Object>> e = extents;
        Map<String, Object> new_item = new HashMap<String, Object>() {{
            put("id", item_id);
            put("xid", xid);
            put("parent_id", parent_id);
            put("name", name);
            put("atime", atime);
            put("mtime", mtime);
            put("crtime", ctime);
            put("size", size);
            put("md5", md5);
            put("type", item_type);
            put("volume", volume);
            put("path", path);
            put("status", status);
            put("extents", e);
        }};
Debug.println(new_item);
        if (!this.seen.contains(new_item)) {
            this.items.add(new_item);
            Map<String, Object> hitem = (Map) ((HashMap) new_item).clone();
            hitem.remove("extents");  // TODO list is not hashable
            this.seen.add(hitem);
        }
    }

    public void save_files(String name, int blocksize, KaitaiStream image_file_io) throws IOException {
        // add suffix if file exists
        String basename = name;
        int i = 0;
        while (Files.exists(Paths.get(name))) {
            name = basename + "_" + i;
            i += 1;
        }

        Files.createDirectories(Paths.get(name));

        for (Map<String, Object> item : this.items) {
            try {
                Path full_path = Paths.get(name, String.valueOf(item.get("volume")), String.valueOf(item.get("xid")), String.valueOf(item.get("path")).substring(1));
                if (full_path != null) {
                    Files.createDirectories(full_path);
                }
                Path file_path = full_path.resolve(String.valueOf(item.get("name")));
Debug.println(file_path);
                if (item.get("type").equals("folder")) {
                    Files.createDirectories(file_path);
                } else {
                    try (OutputStream file_io = Files.newOutputStream(file_path)) {
                        long remaining = (long) item.get("size");
                        for (Map<String, Object> extent : (List<Map>) item.get("extents")) {
                            for (long block_part = 0; block_part < (long) extent.get("length") / blocksize; block_part++) {
                                byte[] data = Block.get_block((int) ((long) extent.get("start") + block_part), blocksize, image_file_io);
                                long chunk_size;
                                if (remaining < blocksize) {
                                    chunk_size = remaining;
                                } else {
                                    chunk_size = blocksize;
                                }
                                remaining -= chunk_size;
                                file_io.write(data, 0, (int) chunk_size);
                            }
                        }
                    }
                }
            } catch (Exception err) {
                logger.warning(String.format("Could not save %s: %s", name, err));
            }
        }
    }

    public void save_bodyfile(String name) throws IOException {
        // add suffix if file exists
        String basename = name;
        int i = 0;
        while (Files.exists(Paths.get(name))) {
            name = basename + "_" + i;
            i += 1;
        }

        try (BufferedWriter csvfile = Files.newBufferedWriter(Paths.get(name))) {
            String[] fieldnames = {
                "md5",
                "name",
                "id",
                "mode",
                "uid",
                "gid",
                "size",
                "atime",
                "mtime",
                "ctime",
                "crtime",
            };
            csvfile.write(String.join("|", fieldnames) + "\n");
            for (Map<String, Object> item : this.items) {
                final Map<String, Object> i2 = (Map) ((HashMap) item).clone();
                long xid = i2.get("xid") != null ? (long) i2.get("xid") : 0;
                i2.put("id", String.format("%s-%s-%s", 0, xid, i2.get("id")));  // TODO : add volume number
                i2.put("name", Paths.get((String) i2.get("path"), (String) i2.get("name")));
                i2.put("mode", i2.get("type").equals("file") ? "f" : "d");
                i2.put("uid", 0);
                i2.put("gid", 0);
                i2.put("ctime", 0);
                String[] row = Arrays.stream(fieldnames).map(k -> String.valueOf(i2.get(k))).toArray(String[]::new);
                csvfile.write(String.join("|", row) + "\n");
            }
        }
    }

    public void save_gtf(String name) throws IOException {
        String basename = name;
        int i = 0;
        while (Files.exists(Paths.get(name))) {
            name = basename + "_" + i;
            i += 1;
        }

        try (BufferedWriter csvfile = Files.newBufferedWriter(Paths.get(name))) {
            String[] fieldnames = {
                "type", "xid", "parent_id", "id", "status", "volume", "path", "name", "atime", "mtime", "crtime",
                "size", "md5"
            };
            csvfile.write(String.join(",", fieldnames));
            for (Map<String, Object> item : this.items) {
                String[] row = Arrays.stream(fieldnames).map(k -> String.valueOf(item.get(k))).toArray(String[]::new);
                csvfile.write(String.join(",", row) + "\n");
            }
        }
    }
}
