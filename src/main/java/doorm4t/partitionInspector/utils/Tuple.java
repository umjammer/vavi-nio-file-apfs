/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.utils;

public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
// https://stackoverflow.com/questions/2670982/using-pairs-or-2-tuples-in-java
