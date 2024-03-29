/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.apfs.kv.keys;

import java.nio.ByteBuffer;

public class EXTENTKey extends FSObjectKey {
    public long logicalAddr;

    public EXTENTKey(ByteBuffer buffer) {
        super(buffer);
        logicalAddr = buffer.getLong();
    }

    @Override
    public String toString() {
        return "EXTENTKey{" +
                hdr +
                "logicalAddr=" + logicalAddr +
                '}';
    }
}
