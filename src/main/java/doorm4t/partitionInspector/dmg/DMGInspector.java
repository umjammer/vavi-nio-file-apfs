/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.dmg;

import doorm4t.partitionInspector.utils.Utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DMGInspector {
    private static final int KOLY_BLOCK_SIZE = 512;
    private static byte[] kolyBlockBytes = new byte[KOLY_BLOCK_SIZE];
    private static doorm4t.partitionInspector.dmg.DMGKolyBlock dmgKolyBlock;
    public static doorm4t.partitionInspector.dmg.Plist plist;

    public DMGInspector(String dmgFile) throws IOException {
        kolyBlockBytes = getKolyBlockBytes(dmgFile);
        dmgKolyBlock = new doorm4t.partitionInspector.dmg.DMGKolyBlock(kolyBlockBytes);

        byte[] dataForkWithPlistBytes = Utils.getImageBytes(dmgFile, (int) dmgKolyBlock.XMLLength + (int) dmgKolyBlock.XMLOffset);
        ByteBuffer dataForkWithPlistBuffer = ByteBuffer.wrap(dataForkWithPlistBytes);

        int dataForkLength = (int) dmgKolyBlock.XMLOffset;
        byte[] bytes = new byte[dataForkLength];
        dataForkWithPlistBuffer.get(bytes, 0, dataForkLength);
        ByteBuffer dataForkBuffer = ByteBuffer.wrap(bytes);

        int plistLength = (int) dmgKolyBlock.XMLLength;
        bytes = new byte[plistLength];
        dataForkWithPlistBuffer.get(bytes, dataForkLength, plistLength);
        ByteBuffer plistBuffer = ByteBuffer.wrap(bytes);

        plist = new doorm4t.partitionInspector.dmg.Plist(plistBuffer, dataForkBuffer);
    }

    private static byte[] getKolyBlockBytes(String dmgFile) throws IOException {
        FileInputStream fis = new FileInputStream(dmgFile);
        fis.getChannel().position(fis.getChannel().size() - KOLY_BLOCK_SIZE);
        byte[] kolyBytes = new byte[KOLY_BLOCK_SIZE];
        fis.read(kolyBytes);
        return kolyBytes;
    }

    public static DMGInspector parseImage(String imgPath) throws IOException {
        return new DMGInspector(imgPath);
    }
}
