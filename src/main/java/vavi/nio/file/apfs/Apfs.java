// This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild

package vavi.nio.file.apfs;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import io.kaitai.struct.ByteBufferKaitaiStream;
import io.kaitai.struct.KaitaiStream;
import io.kaitai.struct.KaitaiStruct;
import vavi.util.Debug;


/**
 * https://developer.apple.com/support/apple-file-system/apple-file-system-reference.pdf
 */
public class Apfs extends KaitaiStruct {
//    public static Apfs fromFile(String fileName) throws IOException {
//        return new Apfs(new ByteBufferKaitaiStream(fileName));
//    }

    public enum Features {
        CASE_INSENSITIVE(1),
        CASE_SENSITIVE(8);

        private final long id;
        Features(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, Features> byId = new HashMap<Long, Features>(2);
        static {
            for (Features e : Features.values())
                byId.put(e.id(), e);
        }
        public static Features byId(long id) { return byId.get(id); }
    }

    public enum InoExtType {
        INO_EXT_TYPE_SNAP_XID(1),
        INO_EXT_TYPE_DELTA_TREE_OID(2),
        INO_EXT_TYPE_DOCUMENT_ID(3),
        INO_EXT_TYPE_NAME(4),
        INO_EXT_TYPE_PREV_FSIZE(5),
        INO_EXT_TYPE_RESERVED_6(6),
        INO_EXT_TYPE_FINDER_INFO(7),
        INO_EXT_TYPE_DSTREAM(8),
        INO_EXT_TYPE_RESERVED_9(9),
        INO_EXT_TYPE_DIR_STATS_KEY(10),
        INO_EXT_TYPE_FS_UUID(11),
        INO_EXT_TYPE_RESERVED_12(12),
        INO_EXT_TYPE_SPARSE_BYTES(13),
        INO_EXT_TYPE_RDEV(14);

        private final long id;
        InoExtType(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, InoExtType> byId = new HashMap<Long, InoExtType>(14);
        static {
            for (InoExtType e : InoExtType.values())
                byId.put(e.id(), e);
        }
        public static InoExtType byId(long id) { return byId.get(id); }
    }

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

        private final long id;
        ObjectType(long id) { this.id = id; }
        public int id() { return (int) id; }
        private static final Map<Long, ObjectType> byId = new HashMap<Long, ObjectType>(28);
        static {
            for (ObjectType e : ObjectType.values())
                byId.put((long) e.id(), e);
        }
        public static ObjectType byId(long id) { return byId.get(id); }
    }

    public enum TreeType {
        OM_TREE(0),
        FS_TREE(1);

        private final long id;
        TreeType(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, TreeType> byId = new HashMap<Long, TreeType>(2);
        static {
            for (TreeType e : TreeType.values())
                byId.put(e.id(), e);
        }
        public static TreeType byId(long id) { return byId.get(id); }
    }

    public enum JObjTypes {
        APFS_TYPE_ANY(0),
        APFS_TYPE_SNAP_METADATA(1),
        APFS_TYPE_EXTENT(2),
        APFS_TYPE_INODE(3),
        APFS_TYPE_XATTR(4),
        APFS_TYPE_SIBLING_LINK(5),
        APFS_TYPE_DSTREAM_ID(6),
        APFS_TYPE_CRYPTO_STATE(7),
        APFS_TYPE_FILE_EXTENT(8),
        APFS_TYPE_DIR_REC(9),
        APFS_TYPE_DIR_STATS(10),
        APFS_TYPE_SNAP_NAME(11),
        APFS_TYPE_SIBLING_MAP(12);

        private final long id;
        JObjTypes(long id) { this.id = id; }
        public int id() { return (int) id; }
        private static final Map<Long, JObjTypes> byId = new HashMap<Long, JObjTypes>(13);
        static {
            for (JObjTypes e : JObjTypes.values())
                byId.put((long) e.id(), e);
        }
        public static JObjTypes byId(long id) { return byId.get(id); }
    }

    public enum JXattrFlags {
        XATTR_DATA_EMBEDDED(2),
        SYMLINK(6);

        private final long id;
        JXattrFlags(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, JXattrFlags> byId = new HashMap<Long, JXattrFlags>(2);
        static {
            for (JXattrFlags e : JXattrFlags.values())
                byId.put(e.id(), e);
        }
        public static JXattrFlags byId(long id) { return byId.get(id); }
    }

    public enum CheckpointMapFlags {
        CHECKPOINT_MAP_LAST(1);

        private final long id;
        CheckpointMapFlags(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, CheckpointMapFlags> byId = new HashMap<Long, CheckpointMapFlags>(1);
        static {
            for (CheckpointMapFlags e : CheckpointMapFlags.values())
                byId.put(e.id(), e);
        }
        public static CheckpointMapFlags byId(long id) { return byId.get(id); }
    }

    public enum ItemType {
        NAMED_PIPE(1),
        CHARACTER_SPECIAL(2),
        DIRECTORY(4),
        BLOCK_SPECIAL(6),
        REGULAR(8),
        SYMBOLIC_LINK(10),
        SOCKET(12),
        WHITEOUT(14);

        private final long id;
        ItemType(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ItemType> byId = new HashMap<Long, ItemType>(8);
        static {
            for (ItemType e : ItemType.values())
                byId.put(e.id(), e);
        }
        public static ItemType byId(long id) { return byId.get(id); }
    }

    public enum ObjectTypeFlags {
        OBJ_VIRTUAL(0),
        OBJ_NONPERSISTENT(2048),
        OBJ_ENCRYPTED(4096),
        OBJ_NOHEADER(8192),
        OBJ_PHYSICAL(16384),
        OBJ_EPHEMERAL(32768);

        private final long id;
        ObjectTypeFlags(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ObjectTypeFlags> byId = new HashMap<Long, ObjectTypeFlags>(6);
        static {
            for (ObjectTypeFlags e : ObjectTypeFlags.values())
                byId.put(e.id(), e);
        }
        public static ObjectTypeFlags byId(long id) { return byId.get(id); }
    }

    public Apfs(KaitaiStream _io) {
        this(_io, null, null);
    }

    public Apfs(KaitaiStream _io, KaitaiStruct _parent) {
        this(_io, _parent, null);
    }

    public Apfs(KaitaiStream _io, KaitaiStruct _parent, Apfs _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root == null ? this : _root;
        _read();
    }
    private void _read() {
        this._raw_block0 = this._io.readBytes(4096);
//Debug.println(StringUtil.getDump(this._raw_block0, 128));
        KaitaiStream _io__raw_block0 = new ByteBufferKaitaiStream2(_raw_block0);
        this.block0 = new Obj(_io__raw_block0, this, _root);
    }
    public static class JSiblingValT extends KaitaiStruct {
        public static JSiblingValT fromFile(String fileName) throws IOException {
            return new JSiblingValT(new ByteBufferKaitaiStream(fileName));
        }

        public JSiblingValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JSiblingValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JSiblingValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.parentId = this._io.readU8le();
            this.nameLen = this._io.readU2le();
            this.name = new String(this._io.readBytes(nameLen()), Charset.forName("utf-8"));
        }
        private long parentId;
        private int nameLen;
        private String name;
        private Apfs _root;
        private NodeEntry _parent;
        public long parentId() { return parentId; }
        public int nameLen() { return nameLen; }
        public String name() { return name; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class JInodeValT extends KaitaiStruct {
        public static JInodeValT fromFile(String fileName) throws IOException {
            return new JInodeValT(new ByteBufferKaitaiStream(fileName));
        }

        public JInodeValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JInodeValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JInodeValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.parentId = this._io.readU8le();
            this.privateId = this._io.readU8le();
            this.createTime = this._io.readU8le();
            this.modTime = this._io.readU8le();
            this.changeTime = this._io.readU8le();
            this.accessTime = this._io.readU8le();
            this.internalFlags = this._io.readU8le();
            this.nchildrenOrNlink = this._io.readU4le();
            this.defaultProtectionClass = this._io.readU4le();
            this.writeGenerationCounter = this._io.readU4le();
            this.bsdFlags = this._io.readU4le();
            this.owner = this._io.readU4le();
            this.group = this._io.readU4le();
            this.mode = this._io.readU2le();
            this.pad1 = this._io.readU2le();
            this.pad2 = this._io.readU8le();
            this.xfields = new XfBlobT(this._io, this, _root);
        }
        private long parentId;
        private long privateId;
        private long createTime;
        private long modTime;
        private long changeTime;
        private long accessTime;
        private long internalFlags;
        private long nchildrenOrNlink;
        private long defaultProtectionClass;
        private long writeGenerationCounter;
        private long bsdFlags;
        private long owner;
        private long group;
        private int mode;
        private int pad1;
        private long pad2;
        private XfBlobT xfields;
        private Apfs _root;
        private NodeEntry _parent;
        public long parentId() { return parentId; }
        public long privateId() { return privateId; }
        public long createTime() { return createTime; }
        public long modTime() { return modTime; }
        public long changeTime() { return changeTime; }
        public long accessTime() { return accessTime; }
        public long internalFlags() { return internalFlags; }
        public long nchildrenOrNlink() { return nchildrenOrNlink; }
        public long defaultProtectionClass() { return defaultProtectionClass; }
        public long writeGenerationCounter() { return writeGenerationCounter; }
        public long bsdFlags() { return bsdFlags; }
        public long owner() { return owner; }
        public long group() { return group; }
        public int mode() { return mode; }
        public int pad1() { return pad1; }
        public long pad2() { return pad2; }
        public XfBlobT xfields() { return xfields; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class JKeyT extends KaitaiStruct {
        public static JKeyT fromFile(String fileName) throws IOException {
            return new JKeyT(new ByteBufferKaitaiStream(fileName));
        }

        public JKeyT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JKeyT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JKeyT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.objIdAndTypeLow = this._io.readU4le();
            this.objIdAndTypeHigh = this._io.readU4le();
        }
        private Integer objId;
        public Integer objId() {
            if (this.objId != null)
                return this.objId;
            int _tmp = (int) ((objIdAndTypeLow() + ((objIdAndTypeHigh() & 268435455) << 32)));
            this.objId = _tmp;
            return this.objId;
        }
        private JObjTypes objType;
        public JObjTypes objType() {
            if (this.objType != null)
                return this.objType;
            this.objType = JObjTypes.byId((objIdAndTypeHigh() >> 28));
            return this.objType;
        }
        private long objIdAndTypeLow;
        private long objIdAndTypeHigh;
        private Apfs _root;
        private NodeEntry _parent;
        public long objIdAndTypeLow() { return objIdAndTypeLow; }
        public long objIdAndTypeHigh() { return objIdAndTypeHigh; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class XfDeviceNode extends KaitaiStruct {
        public static XfDeviceNode fromFile(String fileName) throws IOException {
            return new XfDeviceNode(new ByteBufferKaitaiStream(fileName));
        }

        public XfDeviceNode(KaitaiStream _io) {
            this(_io, null, null);
        }

        public XfDeviceNode(KaitaiStream _io, XfBlobT _parent) {
            this(_io, _parent, null);
        }

        public XfDeviceNode(KaitaiStream _io, XfBlobT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.majorMinor = this._io.readU4le();
        }
        private Integer major;
        public Integer major() {
            if (this.major != null)
                return this.major;
            int _tmp = (int) ((majorMinor() >> 24));
            this.major = _tmp;
            return this.major;
        }
        private Integer minor;
        public Integer minor() {
            if (this.minor != null)
                return this.minor;
            int _tmp = (int) ((majorMinor() & 16777215));
            this.minor = _tmp;
            return this.minor;
        }
        private long majorMinor;
        private Apfs _root;
        private XfBlobT _parent;
        public long majorMinor() { return majorMinor; }
        public Apfs _root() { return _root; }
        public XfBlobT _parent() { return _parent; }
    }
    public static class PointerValT extends KaitaiStruct {
//        public static PointerValT fromFile(String fileName) throws IOException {
//            return new PointerValT(new ByteBufferKaitaiStream(fileName));
//        }

        public PointerValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public PointerValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public PointerValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.pointer = this._io.readU8le();
        }
        private long pointer;
        private Apfs _root;
        private NodeEntry _parent;
        public long pointer() { return pointer; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class JXattrKeyT extends KaitaiStruct {
        public static JXattrKeyT fromFile(String fileName) throws IOException {
            return new JXattrKeyT(new ByteBufferKaitaiStream(fileName));
        }

        public JXattrKeyT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JXattrKeyT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JXattrKeyT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.nameLen = this._io.readU1();
            this.name = new String(KaitaiStream.bytesTerminate(this._io.readBytes(nameLen()), (byte) 0, false), Charset.forName("utf-8"));
        }
        private int nameLen;
        private String name;
        private Apfs _root;
        private NodeEntry _parent;
        public int nameLen() { return nameLen; }
        public String name() { return name; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class CheckpointMappingT extends KaitaiStruct {
//        public static CheckpointMappingT fromFile(String fileName) throws IOException {
//            return new CheckpointMappingT(new ByteBufferKaitaiStream(fileName));
//        }

        public CheckpointMappingT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public CheckpointMappingT(KaitaiStream _io, CheckpointMapPhysT _parent) {
            this(_io, _parent, null);
        }

        public CheckpointMappingT(KaitaiStream _io, CheckpointMapPhysT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.cpmType = ObjectType.byId(this._io.readU2le());
            this.cpmFlags = this._io.readU2le();
            this.cpmSubtype = ObjectType.byId(this._io.readU4le());
            this.cpmSize = this._io.readU4le();
            this.cpmPad = this._io.readU4le();
            this.cpmFsOid = this._io.readU8le();
            this.cpmOid = this._io.readU8le();
            this.cpmPaddr = new OidT(this._io, this, _root);
        }
        private ObjectType cpmType;
        private int cpmFlags;
        private ObjectType cpmSubtype;
        private long cpmSize;
        private long cpmPad;
        private long cpmFsOid;
        private long cpmOid;
        private OidT cpmPaddr;
        private Apfs _root;
        private CheckpointMapPhysT _parent;
        public ObjectType cpmType() { return cpmType; }
        public int cpmFlags() { return cpmFlags; }
        public ObjectType cpmSubtype() { return cpmSubtype; }
        public long cpmSize() { return cpmSize; }
        public long cpmPad() { return cpmPad; }
        public long cpmFsOid() { return cpmFsOid; }
        public long cpmOid() { return cpmOid; }
        public OidT cpmPaddr() { return cpmPaddr; }
        public Apfs _root() { return _root; }
        public CheckpointMapPhysT _parent() { return _parent; }
    }
    public static class NodeEntry extends KaitaiStruct {
//        public static NodeEntry fromFile(String fileName) throws IOException {
//            return new NodeEntry(new ByteBufferKaitaiStream(fileName));
//        }

        public NodeEntry(KaitaiStream _io) {
            this(_io, null, null);
        }

        public NodeEntry(KaitaiStream _io, BtreeNodePhysT _parent) {
            this(_io, _parent, null);
        }

        public NodeEntry(KaitaiStream _io, BtreeNodePhysT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            assert _root != null : "set null";
            _read();
        }
        private void _read() {
            this.keyOffset = this._io.readS2le();
            if ((_parent().btnFlags() & 4) == 0) {
                this.keyLength = this._io.readU2le();
            }
            this.dataOffset = this._io.readS2le();
            if ((_parent().btnFlags() & 4) == 0) {
                this.dataLength = this._io.readU2le();
            }
        }
        private JKeyT jKeyT;
        public JKeyT jKeyT() {
            if (this.jKeyT != null)
                return this.jKeyT;
            long _pos = this._io.pos();
            this._io.seek(((keyOffset() + _parent().btnTableSpace().len()) + 56));
            this.jKeyT = new JKeyT(this._io, this, _root);
            this._io.seek(_pos);
            return this.jKeyT;
        }
        private KaitaiStruct key;
        public KaitaiStruct key() {
            if (this.key != null)
                return this.key;
            long _pos = this._io.pos();
            this._io.seek((((keyOffset() + _parent().btnTableSpace().len()) + 56) + 8));
            {
                JObjTypes on = jKeyT().objType();
                if (on != null) {
                    switch (on) {
                    case APFS_TYPE_FILE_EXTENT: {
                        this.key = new JExtentKeyT(this._io, this, _root);
                        break;
                    }
                    case APFS_TYPE_ANY: {
                        this.key = new OmapKeyT(this._io, this, _root);
                        break;
                    }
                    case APFS_TYPE_SIBLING_LINK: {
                        this.key = new JSiblingKeyT(this._io, this, _root);
                        break;
                    }
                    case APFS_TYPE_DIR_REC: {
                        this.key = new JDrecKeyT(this._io, this, _root);
                        break;
                    }
                    case APFS_TYPE_INODE: {
                        this.key = new JEmptyKeyT(this._io, this, _root);
                        break;
                    }
                    case APFS_TYPE_EXTENT: {
                        this.key = new JEmptyKeyT(this._io, this, _root);
                        break;
                    }
                    case APFS_TYPE_SIBLING_MAP: {
                        this.key = new JEmptyKeyT(this._io, this, _root);
                        break;
                    }
                    case APFS_TYPE_DSTREAM_ID: {
                        this.key = new JEmptyKeyT(this._io, this, _root);
                        break;
                    }
                    case APFS_TYPE_XATTR: {
                        this.key = new JXattrKeyT(this._io, this, _root);
                        break;
                    }
                    default: {
Debug.println(Level.FINE, "unhandled jKeyT().objType(): " + on);
                        break;
                    }
                    }
                }
            }
            this._io.seek(_pos);
            return this.key;
        }
        private KaitaiStruct val;
        public KaitaiStruct val() {
            if (this.val != null)
                return this.val;
            long _pos = this._io.pos();
Debug.printf(Level.FINE, "seek: %08x, %08x, %08x\n",_root.blockSize(), dataOffset(), 40 * (_parent().btnFlags() & 1));
            JObjTypes objType = jKeyT().objType();
Debug.println(Level.FINE, "jKeyT().objType(): " + objType);
            this._io.seek(((_root.blockSize() - dataOffset()) - (40 * (_parent().btnFlags() & 1))));
            switch (objType) {
            case APFS_TYPE_INODE: {
                this.val = new JInodeValT(this._io, this, _root);
                break;
            }
            case APFS_TYPE_DIR_REC: {
                this.val = new JDrecValT(this._io, this, _root);
                break;
            }
            case APFS_TYPE_FILE_EXTENT: {
                this.val = new JExtentValT(this._io, this, _root);
                break;
            }
            case APFS_TYPE_SIBLING_LINK: {
                this.val = new JSiblingValT(this._io, this, _root);
                break;
            }
            case APFS_TYPE_XATTR: {
                this.val = new JXattrValT(this._io, this, _root);
                break;
            }
            case APFS_TYPE_EXTENT: {
                this.val = new JPhysExtValT(this._io, this, _root);
                break;
            }
            case APFS_TYPE_ANY: {
                this.val = new OmapValT(this._io, this, _root);
                break;
            }
            default: {
Debug.println("default: " + objType);
                this.val = new PointerValT(this._io, this, _root);
                break;
            }
            case APFS_TYPE_SIBLING_MAP: {
                this.val = new JSiblingMapValT(this._io, this, _root);
                break;
            }
            case APFS_TYPE_DSTREAM_ID: {
                this.val = new JExtentRefcountValT(this._io, this, _root);
                break;
            }
            }
            this._io.seek(_pos);
            return this.val;
        }
        private short keyOffset;
        private Integer keyLength;
        private short dataOffset;
        private Integer dataLength;
        private Apfs _root;
        private BtreeNodePhysT _parent;
        public short keyOffset() { return keyOffset; }
        public Integer keyLength() { return keyLength; }
        public short dataOffset() { return dataOffset; }
        public Integer dataLength() { return dataLength; }
        public Apfs _root() { return _root; }
        public BtreeNodePhysT _parent() { return _parent; }

        @Override
        public String toString() {
            return String
                    .format("NodeEntry [jKeyT=%s, key=%s, val=%s, keyOffset=%s, keyLength=%s, dataOffset=%s, dataLength=%s]",
                            jKeyT, key, val, keyOffset, keyLength, dataOffset, dataLength);
        }
    }
    public static class OmapValT extends KaitaiStruct {
//        public static OmapValT fromFile(String fileName) throws IOException {
//            return new OmapValT(new ByteBufferKaitaiStream(fileName));
//        }

        public OmapValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public OmapValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public OmapValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.ovFlags = this._io.readU4le();
            this.ovSize = this._io.readU4le();
            this.ovPaddr = new PaddrT(this._io, this, _root);
        }
        private long ovFlags;
        private long ovSize;
        private PaddrT ovPaddr;
        private Apfs _root;
        private NodeEntry _parent;
        public long ovFlags() { return ovFlags; }
        public long ovSize() { return ovSize; }
        public PaddrT ovPaddr() { return ovPaddr; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class XfSize extends KaitaiStruct {
//        public static XfSize fromFile(String fileName) throws IOException {
//            return new XfSize(new ByteBufferKaitaiStream(fileName));
//        }

        public XfSize(KaitaiStream _io) {
            this(_io, null, null);
        }

        public XfSize(KaitaiStream _io, XfBlobT _parent) {
            this(_io, _parent, null);
        }

        public XfSize(KaitaiStream _io, XfBlobT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.size = this._io.readU8le();
            this.storedSize = this._io.readU8le();
            this.unknown16 = this._io.readU8le();
            this.unknownSize = this._io.readU8le();
            this.unknown32 = this._io.readU8le();
        }
        private long size;
        private long storedSize;
        private long unknown16;
        private long unknownSize;
        private long unknown32;
        private Apfs _root;
        private XfBlobT _parent;
        public long size() { return size; }
        public long storedSize() { return storedSize; }
        public long unknown16() { return unknown16; }
        public long unknownSize() { return unknownSize; }
        public long unknown32() { return unknown32; }
        public Apfs _root() { return _root; }
        public XfBlobT _parent() { return _parent; }
    }
    public static class CheckpointMapPhysT extends KaitaiStruct {
//        public static CheckpointMapPhysT fromFile(String fileName) throws IOException {
//            return new CheckpointMapPhysT(new ByteBufferKaitaiStream(fileName));
//        }

        public CheckpointMapPhysT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public CheckpointMapPhysT(KaitaiStream _io, Obj _parent) {
            this(_io, _parent, null);
        }

        public CheckpointMapPhysT(KaitaiStream _io, Obj _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.cpmFlags = CheckpointMapFlags.byId(this._io.readU4le());
            this.cpmCount = this._io.readU4le();
            cpmMap = new ArrayList<CheckpointMappingT>(((Number) (cpmCount())).intValue());
            for (int i = 0; i < cpmCount(); i++) {
                this.cpmMap.add(new CheckpointMappingT(this._io, this, _root));
            }
        }
        private CheckpointMapFlags cpmFlags;
        private long cpmCount;
        private ArrayList<CheckpointMappingT> cpmMap;
        private Apfs _root;
        private Obj _parent;
        public CheckpointMapFlags cpmFlags() { return cpmFlags; }
        public long cpmCount() { return cpmCount; }
        public ArrayList<CheckpointMappingT> cpmMap() { return cpmMap; }
        public Apfs _root() { return _root; }
        public Obj _parent() { return _parent; }
    }
    public static class HistoryValT extends KaitaiStruct {
//        public static HistoryValT fromFile(String fileName) throws IOException {
//            return new HistoryValT(new ByteBufferKaitaiStream(fileName));
//        }

        public HistoryValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public HistoryValT(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public HistoryValT(KaitaiStream _io, KaitaiStruct _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.unknown0 = this._io.readU4le();
            this.unknown4 = this._io.readU4le();
        }
        private long unknown0;
        private long unknown4;
        private Apfs _root;
        private KaitaiStruct _parent;
        public long unknown0() { return unknown0; }
        public long unknown4() { return unknown4; }
        public Apfs _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class ChunkInfoBlock extends KaitaiStruct {
//        public static ChunkInfoBlock fromFile(String fileName) throws IOException {
//            return new ChunkInfoBlock(new ByteBufferKaitaiStream(fileName));
//        }

        public ChunkInfoBlock(KaitaiStream _io) {
            this(_io, null, null);
        }

        public ChunkInfoBlock(KaitaiStream _io, Obj _parent) {
            this(_io, _parent, null);
        }

        public ChunkInfoBlock(KaitaiStream _io, Obj _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.cibIndex = this._io.readU4le();
            this.cibChunkInfoCount = this._io.readU4le();
            cibChunkInfo = new ArrayList<ChunkInfoT>(((Number) (cibChunkInfoCount())).intValue());
            for (int i = 0; i < cibChunkInfoCount(); i++) {
                this.cibChunkInfo.add(new ChunkInfoT(this._io, this, _root));
            }
        }
        private long cibIndex;
        private long cibChunkInfoCount;
        private ArrayList<ChunkInfoT> cibChunkInfo;
        private Apfs _root;
        private Obj _parent;
        public long cibIndex() { return cibIndex; }
        public long cibChunkInfoCount() { return cibChunkInfoCount; }
        public ArrayList<ChunkInfoT> cibChunkInfo() { return cibChunkInfo; }
        public Apfs _root() { return _root; }
        public Obj _parent() { return _parent; }
    }
    public static class Nloc extends KaitaiStruct {
//        public static Nloc fromFile(String fileName) throws IOException {
//            return new Nloc(new ByteBufferKaitaiStream(fileName));
//        }

        public Nloc(KaitaiStream _io) {
            this(_io, null, null);
        }

        public Nloc(KaitaiStream _io, BtreeNodePhysT _parent) {
            this(_io, _parent, null);
        }

        public Nloc(KaitaiStream _io, BtreeNodePhysT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.off = this._io.readU2le();
            this.len = this._io.readU2le();
        }
        private int off;
        private int len;
        private Apfs _root;
        private BtreeNodePhysT _parent;
        public int off() { return off; }
        public int len() { return len; }
        public Apfs _root() { return _root; }
        public BtreeNodePhysT _parent() { return _parent; }
    }
    public static class ApfsModifiedByT extends KaitaiStruct {
//        public static ApfsModifiedByT fromFile(String fileName) throws IOException {
//            return new ApfsModifiedByT(new ByteBufferKaitaiStream(fileName));
//        }

        public ApfsModifiedByT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public ApfsModifiedByT(KaitaiStream _io, ApfsSuperblockT _parent) {
            this(_io, _parent, null);
        }

        public ApfsModifiedByT(KaitaiStream _io, ApfsSuperblockT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.id = this._io.readBytes(32);
            this.timestamp = this._io.readU8le();
            this.lastXid = new XidT(this._io, this, _root);
        }
        private byte[] id;
        private long timestamp;
        private XidT lastXid;
        private Apfs _root;
        private ApfsSuperblockT _parent;
        public byte[] id() { return id; }
        public long timestamp() { return timestamp; }
        public XidT lastXid() { return lastXid; }
        public Apfs _root() { return _root; }
        public ApfsSuperblockT _parent() { return _parent; }
    }
    public static class XfName extends KaitaiStruct {
//        public static XfName fromFile(String fileName) throws IOException {
//            return new XfName(new ByteBufferKaitaiStream(fileName));
//        }

        public XfName(KaitaiStream _io) {
            this(_io, null, null);
        }

        public XfName(KaitaiStream _io, XfBlobT _parent) {
            this(_io, _parent, null);
        }

        public XfName(KaitaiStream _io, XfBlobT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.name = new String(this._io.readBytesTerm((byte) 0, false, true, true), Charset.forName("utf-8"));
        }
        private String name;
        private Apfs _root;
        private XfBlobT _parent;
        public String name() { return name; }
        public Apfs _root() { return _root; }
        public XfBlobT _parent() { return _parent; }
    }
    public static class JDrecKeyT extends KaitaiStruct {
//        public static JDrecKeyT fromFile(String fileName) throws IOException {
//            return new JDrecKeyT(new ByteBufferKaitaiStream(fileName));
//        }

        public JDrecKeyT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JDrecKeyT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JDrecKeyT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.nameLen = this._io.readU1();
            this.hash = this._io.readBytes(3);
            this.name = new String(KaitaiStream.bytesTerminate(this._io.readBytes(nameLen()), (byte) 0, false), Charset.forName("utf-8"));
        }
        private int nameLen;
        private byte[] hash;
        private String name;
        private Apfs _root;
        private NodeEntry _parent;
        public int nameLen() { return nameLen; }
        public byte[] hash() { return hash; }
        public String name() { return name; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class BtreeNodePhysT extends KaitaiStruct {
//        public static BtreeNodePhysT fromFile(String fileName) throws IOException {
//            return new BtreeNodePhysT(new ByteBufferKaitaiStream(fileName));
//        }

        public BtreeNodePhysT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public BtreeNodePhysT(KaitaiStream _io, Obj _parent) {
            this(_io, _parent, null);
        }

        public BtreeNodePhysT(KaitaiStream _io, Obj _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.btnFlags = this._io.readU2le();
            this.btnLevel = this._io.readU2le();
            this.btnNkeys = this._io.readU4le();
            this.btnTableSpace = new Nloc(this._io, this, _root);
            this.btnFreeSpace = new Nloc(this._io, this, _root);
            this.btnKeyFreeList = new Nloc(this._io, this, _root);
            this.btnValFreeList = new Nloc(this._io, this, _root);
            btnData = new ArrayList<NodeEntry>(((Number) (btnNkeys())).intValue());
            for (int i = 0; i < btnNkeys(); i++) {
                this.btnData.add(new NodeEntry(this._io, this, _root));
            }
        }
        private int btnFlags;
        private int btnLevel;
        private long btnNkeys;
        private Nloc btnTableSpace;
        private Nloc btnFreeSpace;
        private Nloc btnKeyFreeList;
        private Nloc btnValFreeList;
        private ArrayList<NodeEntry> btnData;
        private Apfs _root;
        private Obj _parent;
        public int btnFlags() { return btnFlags; }

        /**
         * zero for leaf nodes, > 0 for index nodes
         */
        public int btnLevel() { return btnLevel; }
        public long btnNkeys() { return btnNkeys; }
        public Nloc btnTableSpace() { return btnTableSpace; }
        public Nloc btnFreeSpace() { return btnFreeSpace; }
        public Nloc btnKeyFreeList() { return btnKeyFreeList; }
        public Nloc btnValFreeList() { return btnValFreeList; }
        public ArrayList<NodeEntry> btnData() { return btnData; }
        public Apfs _root() { return _root; }
        public Obj _parent() { return _parent; }
    }
    public static class JExtentRefcountValT extends KaitaiStruct {
//        public static JExtentRefcountValT fromFile(String fileName) throws IOException {
//            return new JExtentRefcountValT(new ByteBufferKaitaiStream(fileName));
//        }

        public JExtentRefcountValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JExtentRefcountValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JExtentRefcountValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.count = this._io.readU4le();
        }
        private long count;
        private Apfs _root;
        private NodeEntry _parent;
        public long count() { return count; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class HistoryKeyT extends KaitaiStruct {
//        public static HistoryKeyT fromFile(String fileName) throws IOException {
//            return new HistoryKeyT(new ByteBufferKaitaiStream(fileName));
//        }

        public HistoryKeyT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public HistoryKeyT(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public HistoryKeyT(KaitaiStream _io, KaitaiStruct _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.xid = this._io.readU8le();
            this.objId = new OidT(this._io, this, _root);
        }
        private long xid;
        private OidT objId;
        private Apfs _root;
        private KaitaiStruct _parent;
        public long xid() { return xid; }
        public OidT objId() { return objId; }
        public Apfs _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class JDrecValT extends KaitaiStruct {
//        public static JDrecValT fromFile(String fileName) throws IOException {
//            return new JDrecValT(new ByteBufferKaitaiStream(fileName));
//        }

        public JDrecValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JDrecValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JDrecValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.fileId = this._io.readU8le();
            this.dateAdded = this._io.readU8le();
            this.flags = new XfBlobT(this._io, this, _root);
        }
        private long fileId;
        private long dateAdded;
        private XfBlobT flags;
        private Apfs _root;
        private NodeEntry _parent;
        public long fileId() { return fileId; }
        public long dateAdded() { return dateAdded; }
        public XfBlobT flags() { return flags; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class Obj extends KaitaiStruct {
//        public static Obj fromFile(String fileName) throws IOException {
//            return new Obj(new ByteBufferKaitaiStream(fileName));
//        }

        public Obj(KaitaiStream _io) {
            this(_io, null, null);
        }

        public Obj(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public Obj(KaitaiStream _io, KaitaiStruct _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.hdr = new ObjPhys(this._io, this, _root);
            {
                ObjectType on = hdr().oType();
                if (on != null) {
                    switch (hdr().oType()) {
                    case OBJECT_TYPE_BTREE_NODE: {
                        this.body = new BtreeNodePhysT(this._io, this, _root);
                        break;
                    }
                    case OBJECT_TYPE_BTREE: {
                        this.body = new BtreeNodePhysT(this._io, this, _root);
                        break;
                    }
                    case OBJECT_TYPE_OMAP: {
                        this.body = new OmapPhysT(this._io, this, _root);
                        break;
                    }
                    case OBJECT_TYPE_SPACEMAN: {
                        this.body = new ChunkInfoBlock(this._io, this, _root);
                        break;
                    }
                    case OBJECT_TYPE_CHECKPOINT_MAP: {
                        this.body = new CheckpointMapPhysT(this._io, this, _root);
                        break;
                    }
                    case OBJECT_TYPE_NX_SUPERBLOCK: {
                        this.body = new NxSuperblockT(this._io, this, _root);
                        break;
                    }
                    case OBJECT_TYPE_FS: {
                        this.body = new ApfsSuperblockT(this._io, this, _root);
                        break;
                    }
                    case OBJECT_TYPE_SPACEMAN_CIB: {
                        this.body = new ChunkInfoT(this._io, this, _root);
                        break;
                    }
                    default:
                        Debug.println(Level.WARNING, "hdr().oType(): " + hdr().oType());
                    }
                }
            }
        }
        private ObjPhys hdr;
        private KaitaiStruct body;
        private Apfs _root;
        private KaitaiStruct _parent;
        public ObjPhys hdr() { return hdr; }
        public KaitaiStruct body() { return body; }
        public Apfs _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
        @Override
        public String toString() {
            return String.format("Obj [hdr=%s, body=%s]", hdr, body);
        }
    }
    public static class JPhysExtValT extends KaitaiStruct {
//        public static JPhysExtValT fromFile(String fileName) throws IOException {
//            return new JPhysExtValT(new ByteBufferKaitaiStream(fileName));
//        }

        public JPhysExtValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JPhysExtValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JPhysExtValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.blockCount = this._io.readU4le();
            this.unknown4 = this._io.readU2le();
            this.blockSize = this._io.readU2le();
            this.inode = this._io.readU8le();
            this.unknown16 = this._io.readU4le();
        }
        private long blockCount;
        private int unknown4;
        private int blockSize;
        private long inode;
        private long unknown16;
        private Apfs _root;
        private NodeEntry _parent;
        public long blockCount() { return blockCount; }
        public int unknown4() { return unknown4; }
        public int blockSize() { return blockSize; }
        public long inode() { return inode; }
        public long unknown16() { return unknown16; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class ObjPhys extends KaitaiStruct {
//        public static ObjPhys fromFile(String fileName) throws IOException {
//            return new ObjPhys(new ByteBufferKaitaiStream(fileName));
//        }

        public ObjPhys(KaitaiStream _io) {
            this(_io, null, null);
        }

        public ObjPhys(KaitaiStream _io, Obj _parent) {
            this(_io, _parent, null);
        }

        public ObjPhys(KaitaiStream _io, Obj _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.oCksum = this._io.readU8le();
            this.oOid = new OidT(this._io, this, _root);
            this.oXid = new XidT(this._io, this, _root);
            this.oType = ObjectType.byId(this._io.readU2le());
            this.oFlags = ObjectTypeFlags.byId(this._io.readU2le());
            this.oSubtype = ObjectType.byId(this._io.readU4le());
        }
        private long oCksum;
        private OidT oOid;
        private XidT oXid;
        private ObjectType oType;
        private ObjectTypeFlags oFlags;
        private ObjectType oSubtype;
        private Apfs _root;
        private Obj _parent;

        /**
         * the fletcher 64 checksum of the object.
         */
        public long oCksum() { return oCksum; }

        /**
         * the objectʼs identifier.
         */
        public OidT oOid() { return oOid; }

        /**
         * the identifier of the most recent transaction that this object was modified in.
         */
        public XidT oXid() { return oXid; }

        /**
         * the objectʼs type and flags.
         */
        public ObjectType oType() { return oType; }
        public ObjectTypeFlags oFlags() { return oFlags; }

        /**
         * the objectʼs subtype.
         */
        public ObjectType oSubtype() { return oSubtype; }
        public Apfs _root() { return _root; }
        public Obj _parent() { return _parent; }

        @Override
        public String toString() {
            return String
                    .format("ObjPhys [oCksum=%s, oOid=%s, oXid=%s, oType=%s, oFlags=%s, oSubtype=%s]",
                            oCksum,
                            oOid,
                            oXid,
                            oType,
                            oFlags,
                            oSubtype);
        }
    }
    public static class ChunkInfoT extends KaitaiStruct {
//        public static ChunkInfoT fromFile(String fileName) throws IOException {
//            return new ChunkInfoT(new ByteBufferKaitaiStream(fileName));
//        }

        public ChunkInfoT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public ChunkInfoT(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public ChunkInfoT(KaitaiStream _io, KaitaiStruct _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.ciXid = this._io.readU8le();
            this.ciAddr = this._io.readU8le();
            this.ciBlockCount = this._io.readU4le();
            this.ciFreeCount = this._io.readU4le();
            this.ciBitmapAddr = this._io.readU8le();
        }
        private long ciXid;
        private long ciAddr;
        private long ciBlockCount;
        private long ciFreeCount;
        private long ciBitmapAddr;
        private Apfs _root;
        private KaitaiStruct _parent;
        public long ciXid() { return ciXid; }
        public long ciAddr() { return ciAddr; }
        public long ciBlockCount() { return ciBlockCount; }
        public long ciFreeCount() { return ciFreeCount; }
        public long ciBitmapAddr() { return ciBitmapAddr; }
        public Apfs _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class SpaceManager extends KaitaiStruct {
//        public static SpaceManager fromFile(String fileName) throws IOException {
//            return new SpaceManager(new ByteBufferKaitaiStream(fileName));
//        }

        public SpaceManager(KaitaiStream _io) {
            this(_io, null, null);
        }

        public SpaceManager(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public SpaceManager(KaitaiStream _io, KaitaiStruct _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.blockSize = this._io.readU4le();
            this.blocksPerChunk = this._io.readU4le();
            this.chunksPerCib = this._io.readU4le();
            this.cibsPerCab = this._io.readU4le();
            this.blockCount = this._io.readU4le();
            this.chunkCount = this._io.readU4le();
            this.cibCount = this._io.readU4le();
            this.cabCount = this._io.readU4le();
            this.entryCount = this._io.readU4le();
            this.unknown68 = this._io.readU4le();
            this.freeBlockCount = this._io.readU8le();
            this.entriesOffset = this._io.readU4le();
            this.unknown84 = this._io.readBytes(92);
            this.prevSpacemanInternalPoolBlock = this._io.readU8le();
        }
        private ArrayList<Long> spacemanInternalPoolBlocks;
        public ArrayList<Long> spacemanInternalPoolBlocks() {
            if (this.spacemanInternalPoolBlocks != null)
                return this.spacemanInternalPoolBlocks;
            long _pos = this._io.pos();
            this._io.seek(entriesOffset());
            spacemanInternalPoolBlocks = new ArrayList<Long>(((Number) (entryCount())).intValue());
            for (int i = 0; i < entryCount(); i++) {
                this.spacemanInternalPoolBlocks.add(this._io.readU8le());
            }
            this._io.seek(_pos);
            return this.spacemanInternalPoolBlocks;
        }
        private long blockSize;
        private long blocksPerChunk;
        private long chunksPerCib;
        private long cibsPerCab;
        private long blockCount;
        private long chunkCount;
        private long cibCount;
        private long cabCount;
        private long entryCount;
        private long unknown68;
        private long freeBlockCount;
        private long entriesOffset;
        private byte[] unknown84;
        private long prevSpacemanInternalPoolBlock;
        private Apfs _root;
        private KaitaiStruct _parent;
        public long blockSize() { return blockSize; }
        public long blocksPerChunk() { return blocksPerChunk; }
        public long chunksPerCib() { return chunksPerCib; }
        public long cibsPerCab() { return cibsPerCab; }
        public long blockCount() { return blockCount; }
        public long chunkCount() { return chunkCount; }
        public long cibCount() { return cibCount; }
        public long cabCount() { return cabCount; }
        public long entryCount() { return entryCount; }
        public long unknown68() { return unknown68; }
        public long freeBlockCount() { return freeBlockCount; }
        public long entriesOffset() { return entriesOffset; }
        public byte[] unknown84() { return unknown84; }
        public long prevSpacemanInternalPoolBlock() { return prevSpacemanInternalPoolBlock; }
        public Apfs _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class XFieldT extends KaitaiStruct {
//        public static XFieldT fromFile(String fileName) throws IOException {
//            return new XFieldT(new ByteBufferKaitaiStream(fileName));
//        }

        public XFieldT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public XFieldT(KaitaiStream _io, XfBlobT _parent) {
            this(_io, _parent, null);
        }

        public XFieldT(KaitaiStream _io, XfBlobT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.xType = InoExtType.byId(this._io.readU1());
            this.xFlags = this._io.readU1();
            this.xSize = this._io.readU2le();
        }
        private InoExtType xType;
        private int xFlags;
        private int xSize;
        private Apfs _root;
        private XfBlobT _parent;
        public InoExtType xType() { return xType; }
        public int xFlags() { return xFlags; }
        public int xSize() { return xSize; }
        public Apfs _root() { return _root; }
        public XfBlobT _parent() { return _parent; }
    }
    public static class ApfsSuperblockT extends KaitaiStruct {
//        public static ApfsSuperblockT fromFile(String fileName) throws IOException {
//            return new ApfsSuperblockT(new ByteBufferKaitaiStream(fileName));
//        }

        public ApfsSuperblockT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public ApfsSuperblockT(KaitaiStream _io, Obj _parent) {
            this(_io, _parent, null);
        }

        public ApfsSuperblockT(KaitaiStream _io, Obj _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.apfsMagic = this._io.readBytes(4);
            this.apfsFsIndex = this._io.readU4le();
            this.apfsFeatures = this._io.readU8le();
            this.apfsReadonlyCompatibleFeatures = this._io.readU8le();
            this.apfsIncompatibleFeatures = this._io.readU8le();
            this.apfsUnmountTime = this._io.readU8le();
            this.apfsFsReserveBlockCount = this._io.readU8le();
            this.apfsFsQuotaBlockCount = this._io.readU8le();
            this.apfsFsAllocCount = this._io.readU8le();
            this.apfsMetaCrypto = this._io.readBytes(32);
            this.apfsOmapOid = new OidT(this._io, this, _root);
            this.apfsRootTreeOid = new OidT(this._io, this, _root);
            this.apfsExtentrefTreeOid = new OidT(this._io, this, _root);
            this.apfsSnapMetaTreeOid = new OidT(this._io, this, _root);
            this.apfsRevertToXid = new XidT(this._io, this, _root);
            this.apfsRevertToSblockOid = new OidT(this._io, this, _root);
            this.apfsNextObjId = this._io.readU8le();
            this.apfsNumFiles = this._io.readU8le();
            this.apfsNumDirectories = this._io.readU8le();
            this.apfsNumSymlinks = this._io.readU8le();
            this.apfsNumOtherFsobjects = this._io.readU8le();
            this.apfsNumSnapshots = this._io.readU8le();
            this.apfsTotalBlocksAlloced = this._io.readU8le();
            this.apfsTotalBlocksFreed = this._io.readU8le();
            this.apfsVolUuid = this._io.readBytes(16);
            this.apfsLastModTime = this._io.readU8le();
            this.apfsFsFlags = this._io.readU8le();
            this.apfsFormattedBy = new ApfsModifiedByT(this._io, this, _root);
            apfsModifiedBy = new ArrayList<ApfsModifiedByT>(((Number) (8)).intValue());
            for (int i = 0; i < 8; i++) {
                this.apfsModifiedBy.add(new ApfsModifiedByT(this._io, this, _root));
            }
            this.apfsVolname = new String(KaitaiStream.bytesTerminate(this._io.readBytes(256), (byte) 0, false), Charset.forName("utf-8"));
            this.apfsNextDocId = this._io.readU4le();
            this.apfsRole = this._io.readU2le();
            this.reserved = this._io.readU2le();
            this.apfsRootToXid = new XidT(this._io, this, _root);
            this.apfsErStateOid = new OidT(this._io, this, _root);
        }
        private byte[] apfsMagic;
        private long apfsFsIndex;
        private long apfsFeatures;
        private long apfsReadonlyCompatibleFeatures;
        private long apfsIncompatibleFeatures;
        private long apfsUnmountTime;
        private long apfsFsReserveBlockCount;
        private long apfsFsQuotaBlockCount;
        private long apfsFsAllocCount;
        private byte[] apfsMetaCrypto;
        private OidT apfsOmapOid;
        private OidT apfsRootTreeOid;
        private OidT apfsExtentrefTreeOid;
        private OidT apfsSnapMetaTreeOid;
        private XidT apfsRevertToXid;
        private OidT apfsRevertToSblockOid;
        private long apfsNextObjId;
        private long apfsNumFiles;
        private long apfsNumDirectories;
        private long apfsNumSymlinks;
        private long apfsNumOtherFsobjects;
        private long apfsNumSnapshots;
        private long apfsTotalBlocksAlloced;
        private long apfsTotalBlocksFreed;
        private byte[] apfsVolUuid;
        private long apfsLastModTime;
        private long apfsFsFlags;
        private ApfsModifiedByT apfsFormattedBy;
        private ArrayList<ApfsModifiedByT> apfsModifiedBy;
        private String apfsVolname;
        private long apfsNextDocId;
        private int apfsRole;
        private int reserved;
        private XidT apfsRootToXid;
        private OidT apfsErStateOid;
        private Apfs _root;
        private Obj _parent;
        public byte[] apfsMagic() { return apfsMagic; }
        public long apfsFsIndex() { return apfsFsIndex; }
        public long apfsFeatures() { return apfsFeatures; }
        public long apfsReadonlyCompatibleFeatures() { return apfsReadonlyCompatibleFeatures; }
        public long apfsIncompatibleFeatures() { return apfsIncompatibleFeatures; }
        public long apfsUnmountTime() { return apfsUnmountTime; }
        public long apfsFsReserveBlockCount() { return apfsFsReserveBlockCount; }
        public long apfsFsQuotaBlockCount() { return apfsFsQuotaBlockCount; }
        public long apfsFsAllocCount() { return apfsFsAllocCount; }
        public byte[] apfsMetaCrypto() { return apfsMetaCrypto; }
        public OidT apfsOmapOid() { return apfsOmapOid; }
        public OidT apfsRootTreeOid() { return apfsRootTreeOid; }
        public OidT apfsExtentrefTreeOid() { return apfsExtentrefTreeOid; }
        public OidT apfsSnapMetaTreeOid() { return apfsSnapMetaTreeOid; }
        public XidT apfsRevertToXid() { return apfsRevertToXid; }
        public OidT apfsRevertToSblockOid() { return apfsRevertToSblockOid; }
        public long apfsNextObjId() { return apfsNextObjId; }
        public long apfsNumFiles() { return apfsNumFiles; }
        public long apfsNumDirectories() { return apfsNumDirectories; }
        public long apfsNumSymlinks() { return apfsNumSymlinks; }
        public long apfsNumOtherFsobjects() { return apfsNumOtherFsobjects; }
        public long apfsNumSnapshots() { return apfsNumSnapshots; }
        public long apfsTotalBlocksAlloced() { return apfsTotalBlocksAlloced; }
        public long apfsTotalBlocksFreed() { return apfsTotalBlocksFreed; }
        public byte[] apfsVolUuid() { return apfsVolUuid; }
        public long apfsLastModTime() { return apfsLastModTime; }
        public long apfsFsFlags() { return apfsFsFlags; }
        public ApfsModifiedByT apfsFormattedBy() { return apfsFormattedBy; }
        public ArrayList<ApfsModifiedByT> apfsModifiedBy() { return apfsModifiedBy; }
        public String apfsVolname() { return apfsVolname; }
        public long apfsNextDocId() { return apfsNextDocId; }
        public int apfsRole() { return apfsRole; }
        public int reserved() { return reserved; }
        public XidT apfsRootToXid() { return apfsRootToXid; }
        public OidT apfsErStateOid() { return apfsErStateOid; }
        public Apfs _root() { return _root; }
        public Obj _parent() { return _parent; }
    }

    /**
     * universal type to address a block: it both parses one u8-sized
     * block address and provides a lazy instance to parse that block
     * right away.
     */
    public static class PaddrT extends KaitaiStruct {
//        public static PaddrT fromFile(String fileName) throws IOException {
//            return new PaddrT(new ByteBufferKaitaiStream(fileName));
//        }

        public PaddrT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public PaddrT(KaitaiStream _io, OmapValT _parent) {
            this(_io, _parent, null);
        }

        public PaddrT(KaitaiStream _io, OmapValT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.val = this._io.readU8le();
        }
        private Obj target;
        public Obj target() {
            if (this.target != null)
                return this.target;
            KaitaiStream io = _root._io();
            long _pos = io.pos();
            io.seek((val() * _root.blockSize()));
            this._raw_target = io.readBytes(_root.blockSize());
            KaitaiStream _io__raw_target = new ByteBufferKaitaiStream2(_raw_target);
            this.target = new Obj(_io__raw_target, this, _root);
            io.seek(_pos);
            return this.target;
        }
        private long val;
        private Apfs _root;
        private OmapValT _parent;
        private byte[] _raw_target;
        public long val() { return val; }
        public Apfs _root() { return _root; }
        public OmapValT _parent() { return _parent; }
        public byte[] _raw_target() { return _raw_target; }
    }
    public static class OmapPhysT extends KaitaiStruct {
//        public static OmapPhysT fromFile(String fileName) throws IOException {
//            return new OmapPhysT(new ByteBufferKaitaiStream(fileName));
//        }

        public OmapPhysT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public OmapPhysT(KaitaiStream _io, Obj _parent) {
            this(_io, _parent, null);
        }

        public OmapPhysT(KaitaiStream _io, Obj _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.omFlags = this._io.readU4le();
            this.omSnapCount = this._io.readU4le();
            this.omTreeType = this._io.readU4le();
            this.omSnapshotTreeType = this._io.readU4le();
            this.omTreeOid = new OidT(this._io, this, _root);
            this.omSnapshotTreeOid = new OidT(this._io, this, _root);
            this.omMostRecentSnap = new XidT(this._io, this, _root);
            this.omPendingRevertMin = new XidT(this._io, this, _root);
            this.omPendingRevertMax = new XidT(this._io, this, _root);
        }
        private long omFlags;
        private long omSnapCount;
        private long omTreeType;
        private long omSnapshotTreeType;
        private OidT omTreeOid;
        private OidT omSnapshotTreeOid;
        private XidT omMostRecentSnap;
        private XidT omPendingRevertMin;
        private XidT omPendingRevertMax;
        private Apfs _root;
        private Obj _parent;
        public long omFlags() { return omFlags; }
        public long omSnapCount() { return omSnapCount; }
        public long omTreeType() { return omTreeType; }
        public long omSnapshotTreeType() { return omSnapshotTreeType; }
        public OidT omTreeOid() { return omTreeOid; }
        public OidT omSnapshotTreeOid() { return omSnapshotTreeOid; }
        public XidT omMostRecentSnap() { return omMostRecentSnap; }
        public XidT omPendingRevertMin() { return omPendingRevertMin; }
        public XidT omPendingRevertMax() { return omPendingRevertMax; }
        public Apfs _root() { return _root; }
        public Obj _parent() { return _parent; }
    }
    public static class JSiblingMapValT extends KaitaiStruct {
//        public static JSiblingMapValT fromFile(String fileName) throws IOException {
//            return new JSiblingMapValT(new ByteBufferKaitaiStream(fileName));
//        }

        public JSiblingMapValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JSiblingMapValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JSiblingMapValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.fileId = this._io.readU8le();
        }
        private long fileId;
        private Apfs _root;
        private NodeEntry _parent;
        public long fileId() { return fileId; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class JSiblingKeyT extends KaitaiStruct {
//        public static JSiblingKeyT fromFile(String fileName) throws IOException {
//            return new JSiblingKeyT(new ByteBufferKaitaiStream(fileName));
//        }

        public JSiblingKeyT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JSiblingKeyT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JSiblingKeyT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.siblingId = this._io.readU8le();
        }
        private long siblingId;
        private Apfs _root;
        private NodeEntry _parent;
        public long siblingId() { return siblingId; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class XfDocumentId extends KaitaiStruct {
//        public static XfDocumentId fromFile(String fileName) throws IOException {
//            return new XfDocumentId(new ByteBufferKaitaiStream(fileName));
//        }

        public XfDocumentId(KaitaiStream _io) {
            this(_io, null, null);
        }

        public XfDocumentId(KaitaiStream _io, XfBlobT _parent) {
            this(_io, _parent, null);
        }

        public XfDocumentId(KaitaiStream _io, XfBlobT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.id = this._io.readU4le();
        }
        private long id;
        private Apfs _root;
        private XfBlobT _parent;
        public long id() { return id; }
        public Apfs _root() { return _root; }
        public XfBlobT _parent() { return _parent; }
    }
    public static class JExtentValT extends KaitaiStruct {
//        public static JExtentValT fromFile(String fileName) throws IOException {
//            return new JExtentValT(new ByteBufferKaitaiStream(fileName));
//        }

        public JExtentValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JExtentValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JExtentValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.len = this._io.readU8le();
            this.physBlockNum = this._io.readU8le();
            this.flags = this._io.readU8le();
        }
        private long len;
        private long physBlockNum;
        private long flags;
        private Apfs _root;
        private NodeEntry _parent;
        public long len() { return len; }
        public long physBlockNum() { return physBlockNum; }
        public long flags() { return flags; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class NxSuperblockT extends KaitaiStruct {
//        public static NxSuperblockT fromFile(String fileName) throws IOException {
//            return new NxSuperblockT(new ByteBufferKaitaiStream(fileName));
//        }

        public NxSuperblockT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public NxSuperblockT(KaitaiStream _io, Obj _parent) {
            this(_io, _parent, null);
        }

        public NxSuperblockT(KaitaiStream _io, Obj _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.nxMagic = this._io.readBytes(4);
            this.nxBlockSize = this._io.readU4le();
            this.nxBlockCount = this._io.readU8le();
            this.nxFeatures = this._io.readU8le();
            this.nxReadonlyCompatibleFeatures = this._io.readU8le();
            this.nxIncompatibleFeatures = this._io.readU8le();
            this.nxUuid = this._io.readBytes(16);
            this.nxNextOid = new OidT(this._io, this, _root);
            this.nxNextXid = new XidT(this._io, this, _root);
            this.nxXpDescBlocks = this._io.readU4le();
            this.nxXpDataBlocks = this._io.readU4le();
            this.nxXpDescBase = this._io.readU8le();
            this.nxXpDataBase = this._io.readU8le();
            this.nxXpDescNext = this._io.readU4le();
            this.nxXpDataNext = this._io.readU4le();
            this.nxXpDescIndex = this._io.readU4le();
            this.nxXpDescLen = this._io.readU4le();
            this.nxXpDataIndex = this._io.readU4le();
            this.nxXpDataLen = this._io.readU4le();
            this.nxSpacemanOid = new OidT(this._io, this, _root);
            this.nxOmapOid = new OidT(this._io, this, _root);
            this.nxReaperOid = new OidT(this._io, this, _root);
            this.nxTestType = this._io.readU4le();
            this.nxMaxFileSystems = this._io.readU4le();
            nxFsOids = new ArrayList<OidT>(((Number) (nxMaxFileSystems())).intValue());
            for (int i = 0; i < nxMaxFileSystems(); i++) {
                this.nxFsOids.add(new OidT(this._io, this, _root));
            }
            nxCounters = new ArrayList<Long>(((Number) (32)).intValue());
            for (int i = 0; i < 32; i++) {
                this.nxCounters.add(this._io.readU8le());
            }
            this.nxBlockedOutPrange = new PrangeT(this._io, this, _root);
            this.nxEvictMappingTreeOid = new OidT(this._io, this, _root);
            this.nxFlags = this._io.readU8le();
            this.nxEfiJumpstart = this._io.readU8le();
            this.nxFusionUuid = this._io.readBytes(16);
            this.nxKeylocker = new PrangeT(this._io, this, _root);
            nxEphemeralInfo = new ArrayList<Long>(((Number) (4)).intValue());
            for (int i = 0; i < 4; i++) {
                this.nxEphemeralInfo.add(this._io.readU8le());
            }
            this.nxTestOid = new OidT(this._io, this, _root);
            this.nxFusionMtOid = new OidT(this._io, this, _root);
            this.nxFusionWbcOid = new OidT(this._io, this, _root);
            this.nxFusionWbc = new PrangeT(this._io, this, _root);
        }
        private Obj checkpointOffset;
        public Obj checkpointOffset() {
            if (this.checkpointOffset != null)
                return this.checkpointOffset;
            long _pos = this._io.pos();
            this._io.seek(((nxXpDescBase() + nxXpDescIndex()) * _root.blockSize()));
            this.checkpointOffset = new Obj(this._io, this, _root);
            this._io.seek(_pos);
            return this.checkpointOffset;
        }
        private Obj spacemanOffset;
        public Obj spacemanOffset() {
            if (this.spacemanOffset != null)
                return this.spacemanOffset;
            long _pos = this._io.pos();
            this._io.seek(((nxXpDataBase() + nxXpDataIndex()) * _root.blockSize()));
            this.spacemanOffset = new Obj(this._io, this, _root);
            this._io.seek(_pos);
            return this.spacemanOffset;
        }
        private byte[] nxMagic;
        private long nxBlockSize;
        private long nxBlockCount;
        private long nxFeatures;
        private long nxReadonlyCompatibleFeatures;
        private long nxIncompatibleFeatures;
        private byte[] nxUuid;
        private OidT nxNextOid;
        private XidT nxNextXid;
        private long nxXpDescBlocks;
        private long nxXpDataBlocks;
        private long nxXpDescBase;
        private long nxXpDataBase;
        private long nxXpDescNext;
        private long nxXpDataNext;
        private long nxXpDescIndex;
        private long nxXpDescLen;
        private long nxXpDataIndex;
        private long nxXpDataLen;
        private OidT nxSpacemanOid;
        private OidT nxOmapOid;
        private OidT nxReaperOid;
        private long nxTestType;
        private long nxMaxFileSystems;
        private ArrayList<OidT> nxFsOids;
        private ArrayList<Long> nxCounters;
        private PrangeT nxBlockedOutPrange;
        private OidT nxEvictMappingTreeOid;
        private long nxFlags;
        private long nxEfiJumpstart;
        private byte[] nxFusionUuid;
        private PrangeT nxKeylocker;
        private ArrayList<Long> nxEphemeralInfo;
        private OidT nxTestOid;
        private OidT nxFusionMtOid;
        private OidT nxFusionWbcOid;
        private PrangeT nxFusionWbc;
        private Apfs _root;
        private Obj _parent;
        public byte[] nxMagic() { return nxMagic; }
        public long nxBlockSize() { return nxBlockSize; }
        public long nxBlockCount() { return nxBlockCount; }
        public long nxFeatures() { return nxFeatures; }
        public long nxReadonlyCompatibleFeatures() { return nxReadonlyCompatibleFeatures; }
        public long nxIncompatibleFeatures() { return nxIncompatibleFeatures; }
        public byte[] nxUuid() { return nxUuid; }
        public OidT nxNextOid() { return nxNextOid; }
        public XidT nxNextXid() { return nxNextXid; }
        public long nxXpDescBlocks() { return nxXpDescBlocks; }
        public long nxXpDataBlocks() { return nxXpDataBlocks; }
        public long nxXpDescBase() { return nxXpDescBase; }
        public long nxXpDataBase() { return nxXpDataBase; }
        public long nxXpDescNext() { return nxXpDescNext; }
        public long nxXpDataNext() { return nxXpDataNext; }
        public long nxXpDescIndex() { return nxXpDescIndex; }
        public long nxXpDescLen() { return nxXpDescLen; }
        public long nxXpDataIndex() { return nxXpDataIndex; }
        public long nxXpDataLen() { return nxXpDataLen; }
        public OidT nxSpacemanOid() { return nxSpacemanOid; }
        public OidT nxOmapOid() { return nxOmapOid; }
        public OidT nxReaperOid() { return nxReaperOid; }
        public long nxTestType() { return nxTestType; }
        public long nxMaxFileSystems() { return nxMaxFileSystems; }
        public ArrayList<OidT> nxFsOids() { return nxFsOids; }
        public ArrayList<Long> nxCounters() { return nxCounters; }
        public PrangeT nxBlockedOutPrange() { return nxBlockedOutPrange; }
        public OidT nxEvictMappingTreeOid() { return nxEvictMappingTreeOid; }
        public long nxFlags() { return nxFlags; }
        public long nxEfiJumpstart() { return nxEfiJumpstart; }
        public byte[] nxFusionUuid() { return nxFusionUuid; }
        public PrangeT nxKeylocker() { return nxKeylocker; }
        public ArrayList<Long> nxEphemeralInfo() { return nxEphemeralInfo; }
        public OidT nxTestOid() { return nxTestOid; }
        public OidT nxFusionMtOid() { return nxFusionMtOid; }
        public OidT nxFusionWbcOid() { return nxFusionWbcOid; }
        public PrangeT nxFusionWbc() { return nxFusionWbc; }
        public Apfs _root() { return _root; }
        public Obj _parent() { return _parent; }

        @Override
        public String toString() {
            return String
                    .format("NxSuperblockT [checkpointOffset=%s, spacemanOffset=%s, nxMagic=%s, nxBlockSize=%s, nxBlockCount=%s, nxFeatures=%s, nxReadonlyCompatibleFeatures=%s, nxIncompatibleFeatures=%s, nxUuid=%s, nxNextOid=%s, nxNextXid=%s, nxXpDescBlocks=%s, nxXpDataBlocks=%s, nxXpDescBase=%s, nxXpDataBase=%s, nxXpDescNext=%s, nxXpDataNext=%s, nxXpDescIndex=%s, nxXpDescLen=%s, nxXpDataIndex=%s, nxXpDataLen=%s, nxSpacemanOid=%s, nxOmapOid=%s, nxReaperOid=%s, nxTestType=%s, nxMaxFileSystems=%s, nxFsOids=%s, nxCounters=%s, nxBlockedOutPrange=%s, nxEvictMappingTreeOid=%s, nxFlags=%s, nxEfiJumpstart=%s, nxFusionUuid=%s, nxKeylocker=%s, nxEphemeralInfo=%s, nxTestOid=%s, nxFusionMtOid=%s, nxFusionWbcOid=%s, nxFusionWbc=%s]",
                            checkpointOffset,
                            spacemanOffset,
                            Arrays.toString(nxMagic),
                            nxBlockSize,
                            nxBlockCount,
                            nxFeatures,
                            nxReadonlyCompatibleFeatures,
                            nxIncompatibleFeatures,
                            Arrays.toString(nxUuid),
                            nxNextOid,
                            nxNextXid,
                            nxXpDescBlocks,
                            nxXpDataBlocks,
                            nxXpDescBase,
                            nxXpDataBase,
                            nxXpDescNext,
                            nxXpDataNext,
                            nxXpDescIndex,
                            nxXpDescLen,
                            nxXpDataIndex,
                            nxXpDataLen,
                            nxSpacemanOid,
                            nxOmapOid,
                            nxReaperOid,
                            nxTestType,
                            nxMaxFileSystems,
                            nxFsOids,
                            nxCounters,
                            nxBlockedOutPrange,
                            nxEvictMappingTreeOid,
                            nxFlags,
                            nxEfiJumpstart,
                            Arrays.toString(nxFusionUuid),
                            nxKeylocker,
                            nxEphemeralInfo,
                            nxTestOid,
                            nxFusionMtOid,
                            nxFusionWbcOid,
                            nxFusionWbc);
        }
    }
    public static class XfSparseSize extends KaitaiStruct {
//        public static XfSparseSize fromFile(String fileName) throws IOException {
//            return new XfSparseSize(new ByteBufferKaitaiStream(fileName));
//        }

        public XfSparseSize(KaitaiStream _io) {
            this(_io, null, null);
        }

        public XfSparseSize(KaitaiStream _io, XfBlobT _parent) {
            this(_io, _parent, null);
        }

        public XfSparseSize(KaitaiStream _io, XfBlobT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.size = this._io.readU8le();
        }
        private long size;
        private Apfs _root;
        private XfBlobT _parent;
        public long size() { return size; }
        public Apfs _root() { return _root; }
        public XfBlobT _parent() { return _parent; }
    }
    public static class OmapKeyT extends KaitaiStruct {
//        public static OmapKeyT fromFile(String fileName) throws IOException {
//            return new OmapKeyT(new ByteBufferKaitaiStream(fileName));
//        }

        public OmapKeyT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public OmapKeyT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public OmapKeyT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.okOid = new OidT(this._io, this, _root);
            this.okXid = new XidT(this._io, this, _root);
        }
        private OidT okOid;
        private XidT okXid;
        private Apfs _root;
        private NodeEntry _parent;
        public OidT okOid() { return okOid; }
        public XidT okXid() { return okXid; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class XfBlobT extends KaitaiStruct {
//        public static XfBlobT fromFile(String fileName) throws IOException {
//            return new XfBlobT(new ByteBufferKaitaiStream(fileName));
//        }

        public XfBlobT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public XfBlobT(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public XfBlobT(KaitaiStream _io, KaitaiStruct _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.xfNumExts = this._io.readU2le();
            this.xfUsedData = this._io.readU2le();
            xfData = new ArrayList<XFieldT>(((Number) (xfNumExts())).intValue());
            for (int i = 0; i < xfNumExts(); i++) {
                this.xfData.add(new XFieldT(this._io, this, _root));
            }
            this._raw_xf = new ArrayList<byte[]>(((Number) (xfNumExts())).intValue());
            xf = new ArrayList<Object>(((Number) (xfNumExts())).intValue());
            for (int i = 0; i < xfNumExts(); i++) {
                {
                    InoExtType on = xfData().get((int) i).xType();
                    if (on != null) {
                        switch (xfData().get((int) i).xType()) {
                        case INO_EXT_TYPE_DSTREAM: {
                            this._raw_xf.add(this._io.readBytes((xfData().get((int) i).xSize() + KaitaiStream.mod((8 - xfData().get((int) i).xSize()), 8))));
                            KaitaiStream _io__raw_xf = new ByteBufferKaitaiStream2(_raw_xf.get(_raw_xf.size() - 1));
                            this.xf.add(new XfSize(_io__raw_xf, this, _root));
                            break;
                        }
                        case INO_EXT_TYPE_SPARSE_BYTES: {
                            this._raw_xf.add(this._io.readBytes((xfData().get((int) i).xSize() + KaitaiStream.mod((8 - xfData().get((int) i).xSize()), 8))));
                            KaitaiStream _io__raw_xf = new ByteBufferKaitaiStream2(_raw_xf.get(_raw_xf.size() - 1));
                            this.xf.add(new XfSparseSize(_io__raw_xf, this, _root));
                            break;
                        }
                        case INO_EXT_TYPE_RDEV: {
                            this._raw_xf.add(this._io.readBytes((xfData().get((int) i).xSize() + KaitaiStream.mod((8 - xfData().get((int) i).xSize()), 8))));
                            KaitaiStream _io__raw_xf = new ByteBufferKaitaiStream2(_raw_xf.get(_raw_xf.size() - 1));
                            this.xf.add(new XfDeviceNode(_io__raw_xf, this, _root));
                            break;
                        }
                        case INO_EXT_TYPE_DOCUMENT_ID: {
                            this._raw_xf.add(this._io.readBytes((xfData().get((int) i).xSize() + KaitaiStream.mod((8 - xfData().get((int) i).xSize()), 8))));
                            KaitaiStream _io__raw_xf = new ByteBufferKaitaiStream2(_raw_xf.get(_raw_xf.size() - 1));
                            this.xf.add(new XfDocumentId(_io__raw_xf, this, _root));
                            break;
                        }
                        case INO_EXT_TYPE_NAME: {
                            this._raw_xf.add(this._io.readBytes((xfData().get((int) i).xSize() + KaitaiStream.mod((8 - xfData().get((int) i).xSize()), 8))));
                            KaitaiStream _io__raw_xf = new ByteBufferKaitaiStream2(_raw_xf.get(_raw_xf.size() - 1));
                            this.xf.add(new XfName(_io__raw_xf, this, _root));
                            break;
                        }
                        default: {
                            this.xf.add(this._io.readBytes((xfData().get((int) i).xSize() + KaitaiStream.mod((8 - xfData().get((int) i).xSize()), 8))));
                            break;
                        }
                        }
                    } else {
                        this.xf.add(this._io.readBytes((xfData().get((int) i).xSize() + KaitaiStream.mod((8 - xfData().get((int) i).xSize()), 8))));
                    }
                }
            }
        }
        private int xfNumExts;
        private int xfUsedData;
        private ArrayList<XFieldT> xfData;
        private ArrayList<Object> xf;
        private Apfs _root;
        private KaitaiStruct _parent;
        private ArrayList<byte[]> _raw_xf;

        /**
         * file 0x02 or folder 0x01 cmp. tn1150
         */
        public int xfNumExts() { return xfNumExts; }
        public int xfUsedData() { return xfUsedData; }
        public ArrayList<XFieldT> xfData() { return xfData; }
        public ArrayList<Object> xf() { return xf; }
        public Apfs _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
        public ArrayList<byte[]> _raw_xf() { return _raw_xf; }
    }

    /**
     * similar to paddr_t
     */
    public static class OidT extends KaitaiStruct {
//        public static OidT fromFile(String fileName) throws IOException {
//            return new OidT(new ByteBufferKaitaiStream(fileName));
//        }

        public OidT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public OidT(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public OidT(KaitaiStream _io, KaitaiStruct _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.val = this._io.readU8le();
        }
        private Obj target;
        public Obj target() {
            if (this.target != null)
                return this.target;
            KaitaiStream io = _root._io();
            long _pos = io.pos();
            io.seek((val() * _root.blockSize()));
            this._raw_target = io.readBytes(_root.blockSize());
//Debug.println(_raw_target.length + "\n" + StringUtil.getDump(_raw_target));
            KaitaiStream _io__raw_target = new ByteBufferKaitaiStream2(_raw_target);
            this.target = new Obj(_io__raw_target, this, _root);
            io.seek(_pos);
            return this.target;
        }
        private long val;
        private Apfs _root;
        private KaitaiStruct _parent;
        private byte[] _raw_target;
        public long val() { return val; }
        public Apfs _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
        public byte[] _raw_target() { return _raw_target; }
        @Override
        public String toString() {
            return String.format("OidT [target=%s, val=%s]", target, val);
        }
    }
    public static class PrangeT extends KaitaiStruct {
//        public static PrangeT fromFile(String fileName) throws IOException {
//            return new PrangeT(new ByteBufferKaitaiStream(fileName));
//        }

        public PrangeT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public PrangeT(KaitaiStream _io, NxSuperblockT _parent) {
            this(_io, _parent, null);
        }

        public PrangeT(KaitaiStream _io, NxSuperblockT _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.prStartPaddr = this._io.readU8le();
            this.prBlockCount = this._io.readU8le();
        }
        private long prStartPaddr;
        private long prBlockCount;
        private Apfs _root;
        private NxSuperblockT _parent;
        public long prStartPaddr() { return prStartPaddr; }
        public long prBlockCount() { return prBlockCount; }
        public Apfs _root() { return _root; }
        public NxSuperblockT _parent() { return _parent; }
    }
    public static class JExtentKeyT extends KaitaiStruct {
//        public static JExtentKeyT fromFile(String fileName) throws IOException {
//            return new JExtentKeyT(new ByteBufferKaitaiStream(fileName));
//        }

        public JExtentKeyT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JExtentKeyT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JExtentKeyT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.offset = this._io.readU8le();
        }
        private long offset;
        private Apfs _root;
        private NodeEntry _parent;
        public long offset() { return offset; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class JXattrValT extends KaitaiStruct {
//        public static JXattrValT fromFile(String fileName) throws IOException {
//            return new JXattrValT(new ByteBufferKaitaiStream(fileName));
//        }

        public JXattrValT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JXattrValT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JXattrValT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.flags = JXattrFlags.byId(this._io.readU2le());
            this.xdataLength = this._io.readU2le();
            {
                JXattrFlags on = flags();
                if (on != null) {
                    switch (flags()) {
                    case SYMLINK: {
                        this.xdata = new String(KaitaiStream.bytesTerminate(this._io.readBytes(xdataLength()), (byte) 0, false), Charset.forName("utf-8"));
                        break;
                    }
                    default: {
                        this.xdata = this._io.readBytes(xdataLength());
                        break;
                    }
                    }
                } else {
                    this.xdata = this._io.readBytes(xdataLength());
                }
            }
        }
        private JXattrFlags flags;
        private int xdataLength;
        private Object xdata;
        private Apfs _root;
        private NodeEntry _parent;
        public JXattrFlags flags() { return flags; }
        public int xdataLength() { return xdataLength; }
        public Object xdata() { return xdata; }
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    public static class XidT extends KaitaiStruct {
//        public static XidT fromFile(String fileName) throws IOException {
//            return new XidT(new ByteBufferKaitaiStream(fileName));
//        }

        public XidT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public XidT(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public XidT(KaitaiStream _io, KaitaiStruct _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.val = this._io.readU8le();
        }
        private long val;
        private Apfs _root;
        private KaitaiStruct _parent;
        public long val() { return val; }
        public Apfs _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
        @Override
        public String toString() {
            return String.format("XidT [val=%s]", val);
        }
    }
    public static class JEmptyKeyT extends KaitaiStruct {
//        public static JEmptyKeyT fromFile(String fileName) throws IOException {
//            return new JEmptyKeyT(new ByteBufferKaitaiStream(fileName));
//        }

        public JEmptyKeyT(KaitaiStream _io) {
            this(_io, null, null);
        }

        public JEmptyKeyT(KaitaiStream _io, NodeEntry _parent) {
            this(_io, _parent, null);
        }

        public JEmptyKeyT(KaitaiStream _io, NodeEntry _parent, Apfs _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
        }
        private Apfs _root;
        private NodeEntry _parent;
        public Apfs _root() { return _root; }
        public NodeEntry _parent() { return _parent; }
    }
    private Long blockSize;
    public Long blockSize() {
        if (this.blockSize != null)
            return this.blockSize;
        long _tmp = (long) (((NxSuperblockT) (_root.block0().body())).nxBlockSize());
        this.blockSize = _tmp;
        return this.blockSize;
    }
    private Obj block0;
    private Apfs _root;
    private KaitaiStruct _parent;
    private byte[] _raw_block0;
    public Obj block0() { return block0; }
    public Apfs _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
    public byte[] _raw_block0() { return _raw_block0; }
}
