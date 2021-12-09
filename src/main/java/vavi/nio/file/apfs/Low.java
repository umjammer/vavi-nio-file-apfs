package vavi.nio.file.apfs;

import java.util.List;

import vavi.nio.file.apfs.kaitai.Apfs;
import vavi.nio.file.apfs.kaitai.Apfs.ApfsSuperblockT;
import vavi.nio.file.apfs.kaitai.Apfs.BtreeNodePhysT;
import vavi.nio.file.apfs.kaitai.Apfs.NodeEntry;
import vavi.nio.file.apfs.kaitai.Apfs.NxSuperblockT;
import vavi.nio.file.apfs.kaitai.Apfs.Obj;
import vavi.nio.file.apfs.kaitai.Apfs.OmapPhysT;


/**
 * low level api methods to apfs
 */
class Low {
    /** get entries of the container superblock */
    static List<NodeEntry> get_nxsb_objects(Obj nxsb) {

        // get omap in container superblock
        Obj object_map = ((NxSuperblockT) nxsb.body()).nxOmapOid().target();
System.err.println("object type: " + object_map.hdr().oType());
        Obj object_map_root;
        if (object_map.hdr().oType() == Apfs.ObjectType.OBJECT_TYPE_BTREE || object_map.hdr().oType() == Apfs.ObjectType.OBJECT_TYPE_BTREE_NODE) {
            object_map_root = object_map;
        } else {
            object_map_root = ((OmapPhysT) object_map.body()).omTreeOid().target();
        }

        // iterate omap
        return ((BtreeNodePhysT) object_map_root.body()).btnData();
     }

    /** get entries of the volume superblock */
    static List<NodeEntry> get_apsb_objects(Obj apsb) {

        Obj object_map = ((ApfsSuperblockT) apsb.body()).apfsOmapOid().target();
System.err.println("object type: " + object_map.hdr().oType());
        Obj object_map_root;
        if (object_map.hdr().oType() == Apfs.ObjectType.OBJECT_TYPE_BTREE
                || object_map.hdr().oType() == Apfs.ObjectType.OBJECT_TYPE_BTREE_NODE) {
            object_map_root = object_map;
        } else {
            object_map_root = ((OmapPhysT) object_map.body()).omTreeOid().target();
        }

        // iterate omap
        return ((BtreeNodePhysT) object_map_root.body()).btnData();
    }
}