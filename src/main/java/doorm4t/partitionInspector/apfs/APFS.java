/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.apfs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;

import doorm4t.partitionInspector.utils.Utils;
import vavi.util.Debug;


public class APFS {

    String imagePath;
    private ApfsContainer containerSb;
    private int blockSize;
    public ArrayList<ApfsVolume> volumes;

    public APFS(String imagePath) throws IOException {
        this.imagePath = imagePath;

        // Parse the Container Superblock (CSB)
        containerSb = ApfsContainer.parseContainer(imagePath);
Debug.println(Level.FINER, containerSb.toString());
        blockSize = containerSb.nx_block_size;

        volumes = getAPFSVolumes();
        ApfsVolume volume = volumes.get(0);
Debug.println(Level.FINER, volume);
    }

//    public ArrayList<FSKeyValue> getFSRecords(int volIndex) throws IOException {
//        ApfsVolume volume = volumes.get(volIndex);
//        // TODO: Get records
//    }

    // Parse the Volume Superblock (VSB)
    // Get the VSB physical address by searching for nx_fs_oid in the CSB OMAP
    // nx_fs_oid: contains OIDs for VSBs - see APFS Reference pg. 32
    private ArrayList<ApfsVolume> getAPFSVolumes() throws IOException {
        long[] volumeOIDs = containerSb.nx_fs_oid;
        int blockSize = containerSb.nx_block_size;

        ArrayList<ApfsVolume> vols = new ArrayList<>();
        for (long volOID : volumeOIDs) {
            int vsbOffset = containerSb.csbOMap.parsedOmap.get(volOID).intValue() * blockSize;
            ByteBuffer volumeSbBuffer = Utils.GetBuffer(imagePath, vsbOffset, blockSize);
            vols.add(new ApfsVolume(volumeSbBuffer, blockSize, imagePath));
        }

        return vols;
    }
}

// Single Files
// Map Object Identifier -> Inode Records
// Map Object Identifier -> Extend Records

// Directories
// Map Object Identifier -> Drec Records

