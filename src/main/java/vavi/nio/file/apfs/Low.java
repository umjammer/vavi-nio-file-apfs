package vavi.nio.file.apfs;

import java.util.List;

import vavi.nio.file.apfs.Apfs.ApfsSuperblockT;
import vavi.nio.file.apfs.Apfs.BtreeNodePhysT;
import vavi.nio.file.apfs.Apfs.NodeEntry;
import vavi.nio.file.apfs.Apfs.NxSuperblockT;
import vavi.nio.file.apfs.Apfs.Obj;
import vavi.nio.file.apfs.Apfs.OmapPhysT;
import vavi.util.Debug;


/**
 * low level api methods to apfs
 */
class Low {

    /** get entries of the container superblock */
    static List<NodeEntry> get_nxsb_objects(Obj nxsb) {

        // get omap in container superblock
        Obj object_map = ((NxSuperblockT) nxsb.body()).nxOmapOid().target();
Debug.println("object type: " + object_map.hdr().oType());
        Obj object_map_root;
        if (object_map.hdr().oType() == Apfs.ObjectType.OBJECT_TYPE_BTREE || object_map.hdr().oType() == Apfs.ObjectType.OBJECT_TYPE_BTREE_NODE) {
            object_map_root = object_map;
Debug.println("1: object_map_root: " + object_map_root);
        } else {
            object_map_root = ((OmapPhysT) object_map.body()).omTreeOid().target();
Debug.println("2: object_map_root: " + object_map_root);
        }

        // iterate omap
Debug.println("RET: " + ((BtreeNodePhysT) object_map_root.body()).btnData().size());
        return ((BtreeNodePhysT) object_map_root.body()).btnData();
     }

    /** get entries of the volume superblock */
    static List<NodeEntry> get_apsb_objects(Obj apsb) {

        Obj object_map = ((ApfsSuperblockT) apsb.body()).apfsOmapOid().target();
Debug.println("object type: " + object_map.hdr().oType());
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