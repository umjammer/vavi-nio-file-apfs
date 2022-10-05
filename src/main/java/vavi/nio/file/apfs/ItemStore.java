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

    private static final Logger logger = Logger.getLogger(ItemStore.class.getName());

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

    void addItem(String item_type,
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
Debug.printf("name not found (%d, %d, %d)\n", xid, parent_id, item_id);
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
//Debug.println(new_item);
        if (!this.seen.contains(new_item)) {
            this.items.add(new_item);
            @SuppressWarnings({"unchecked", "rawtypes"})
            Map<String, Object> hItem = (Map) ((HashMap<?, ?>) new_item).clone();
            hItem.remove("extents");  // TODO list is not hashable
            this.seen.add(hItem);
        }
    }

    @SuppressWarnings("unchecked")
    public void saveFiles(String name, int blockSize, KaitaiStream io) throws IOException {
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
                Path fullPath = Paths.get(name, String.valueOf(item.get("volume")), String.valueOf(item.get("xid")), String.valueOf(item.get("path")).substring(1));
                Files.createDirectories(fullPath);
                Path filePath = fullPath.resolve(String.valueOf(item.get("name")));
Debug.println(filePath);
                if (item.get("type").equals("folder")) {
                    Files.createDirectories(filePath);
                } else {
                    try (OutputStream os = Files.newOutputStream(filePath)) {
                        long remaining = (long) item.get("size");
Debug.println(((List<Map<String, Object>>) item.get("extents")).size());
                        for (Map<String, Object> extent : (List<Map<String, Object>>) item.get("extents")) {
                            for (long blockPart = 0; blockPart < (long) extent.get("length") / blockSize; blockPart++) {
                                byte[] data = Block.get_block((int) ((long) extent.get("start") + blockPart), blockSize, io);
                                long chunkSize;
                                if (remaining < blockSize) {
                                    chunkSize = remaining;
                                } else {
                                    chunkSize = blockSize;
                                }
                                remaining -= chunkSize;
                                os.write(data, 0, (int) chunkSize);
                            }
                        }
                    }
                }
            } catch (Exception err) {
                logger.warning(String.format("Could not save %s: %s", name, err));
            }
        }
    }

    public void saveBodyFile(String name) throws IOException {
        // add suffix if file exists
        String basename = name;
        int i = 0;
        while (Files.exists(Paths.get(name))) {
            name = basename + "_" + i;
            i += 1;
        }

        try (BufferedWriter csvFile = Files.newBufferedWriter(Paths.get(name))) {
            String[] fieldNames = {
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
            csvFile.write(String.join("|", fieldNames) + "\n");
            for (Map<String, Object> item : this.items) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> i2 = (Map<String, Object>) ((HashMap<String, Object>) item).clone();
                long xid = i2.get("xid") != null ? (long) i2.get("xid") : 0;
                i2.put("id", String.format("%s-%s-%s", 0, xid, i2.get("id")));  // TODO : add volume number
                i2.put("name", Paths.get((String) i2.get("path"), (String) i2.get("name")));
                i2.put("mode", i2.get("type").equals("file") ? "f" : "d");
                i2.put("uid", 0);
                i2.put("gid", 0);
                i2.put("ctime", 0);
                String[] row = Arrays.stream(fieldNames).map(k -> String.valueOf(i2.get(k))).toArray(String[]::new);
                csvFile.write(String.join("|", row) + "\n");
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

        try (BufferedWriter csvFile = Files.newBufferedWriter(Paths.get(name))) {
            String[] fieldNames = {
                "type", "xid", "parent_id", "id", "status", "volume", "path", "name", "atime", "mtime", "crtime",
                "size", "md5"
            };
            csvFile.write(String.join(",", fieldNames));
            for (Map<String, Object> item : this.items) {
                String[] row = Arrays.stream(fieldNames).map(k -> String.valueOf(item.get(k))).toArray(String[]::new);
                csvFile.write(String.join(",", row) + "\n");
            }
        }
    }
}
