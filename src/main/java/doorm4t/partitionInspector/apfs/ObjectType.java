/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package doorm4t.partitionInspector.apfs;

import java.util.Arrays;


/**
 * ObjectType.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/10/10 nsano initial version <br>
 */
public enum ObjectType {

    OBJECT_TYPE_INVALID(0),
    OBJECT_TYPE_NX_SUPERBLOCK(1),
    OBJECT_TYPE_BTREE(2),
    OBJECT_TYPE_BTREE_NODE(3),
    OBJECT_TYPE_SPACEMAN(5),
    OBJECT_TYPE_SPACEMAN_CAB(6),
    OBJECT_TYPE_SPACEMAN_CIB(7),
    OBJECT_TYPE_SPACEMAN_BITMAP(8),
    OBJECT_TYPE_SPACEMAN_FREE_QUEUE(9),
    OBJECT_TYPE_EXTENT_LIST_TREE(10),
    OBJECT_TYPE_OMAP(11),
    OBJECT_TYPE_CHECKPOINT_MAP(12),
    OBJECT_TYPE_FS(13),
    OBJECT_TYPE_FSTREE(14),
    OBJECT_TYPE_BLOCKREFTREE(15),
    OBJECT_TYPE_SNAPMETATREE(16),
    OBJECT_TYPE_NX_REAPER(17),
    OBJECT_TYPE_NX_REAP_LIST(18),
    OBJECT_TYPE_OMAP_SNAPSHOT(19),
    OBJECT_TYPE_EFI_JUMPSTART(20),
    OBJECT_TYPE_FUSION_MIDDLE_TREE(21),
    OBJECT_TYPE_NX_FUSION_WBC(22),
    OBJECT_TYPE_NX_FUSION_WBC_LIST(23),
    OBJECT_TYPE_ER_STATE(24),
    OBJECT_TYPE_GBITMAP(25),
    OBJECT_TYPE_GBITMAP_TREE(26),
    OBJECT_TYPE_GBITMAP_BLOCK(27),
    OBJECT_TYPE_TEST(255);

    final int id;

    ObjectType(int id) {
        this.id = id;
    }

    public static ObjectType byId(int id) {
        return Arrays.stream(values()).filter(e -> e.id == id).findFirst().get();
    }
}
