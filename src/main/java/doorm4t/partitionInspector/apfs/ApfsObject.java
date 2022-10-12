/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package doorm4t.partitionInspector.apfs;

/**
 * ApfsObject.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-10-11 nsano initial version <br>
 */
public interface ApfsObject {

    /** */
    BlockHeader getHeader();

    /** */
    void setHeader(BlockHeader header);
}
