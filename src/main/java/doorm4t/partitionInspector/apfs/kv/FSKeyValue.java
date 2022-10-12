/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.apfs.kv;

import doorm4t.partitionInspector.apfs.kv.keys.FSObjectKey;
import doorm4t.partitionInspector.apfs.kv.values.FSObjectValue;

public class FSKeyValue {
    public FSObjectKey key;
    public FSObjectValue value;

    public FSKeyValue(FSObjectKey key, FSObjectValue value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "\tkey=" + key +
                "\n\t\tvalue=" + value + "\n";
    }
}
