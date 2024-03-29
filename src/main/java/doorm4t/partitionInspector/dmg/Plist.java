/*
 * https://github.com/DOORM4T/partition-inspector
 */

package doorm4t.partitionInspector.dmg;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Plist {

    public ArrayList<String> fileNames = new ArrayList<>();

    public Plist(ByteBuffer pListBuffer, ByteBuffer dataForkBuffer) {

        try {
            Document d = getPlistXMLDocument(pListBuffer);

            List<String> base64Data = getDataByKeyName(d, "Data");
            List<String> cfNames = getDataByKeyName(d, "CFName");

            int numMishBlocks = cfNames.size();

            File decompressedOutputFolder = createDecompressedOutputFolder();

            for (int i = 0; i < numMishBlocks; i++) {
                String s = base64Data.get(i).replaceAll("[\\n\\s]", "");
                byte[] mishBytes = Base64.getDecoder().decode(s);
                MishBlock block = new MishBlock(mishBytes);
                //  System.out.println(block);

                // DECOMPRESS
                MishBlock.BLKXChunkEntry[] blkxChunks = block.getBlkxChunkEntries();

                String fileName = i + "_" + cfNames.get(i).replaceAll("[\\(\\):\\s]", "");
                fileNames.add(fileName);
                String pathName = decompressedOutputFolder.getCanonicalPath() + File.separator + fileName;
                File decompressedMishFile = new File(pathName);
                FileOutputStream decompressedChunkWriter = new FileOutputStream(decompressedMishFile.getAbsolutePath());

                if (blkxChunks.length > 0) {
                    for (int chunkIndex = 0; chunkIndex < blkxChunks.length; chunkIndex++) {
                        try {
                            byte[] decompressedBytes = BLKXBlockDecompress.decompressBLKXBlock(dataForkBuffer, blkxChunks[chunkIndex]);
                            decompressedChunkWriter.write(decompressedBytes);
                        } catch (Exception e) {
                        }
                    }
                }

                decompressedChunkWriter.close();
            }

        } catch (Exception e) {
        }
    }

    public ArrayList<String> getFilenames(){
        return fileNames;
    }

    /**
     * @param pListBuffer ByteBuffer slice of plist bytes
     * @return parsed XML Document
     */
    private Document getPlistXMLDocument(ByteBuffer pListBuffer) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        byte[] plistBytes = new byte[pListBuffer.remaining()];
        pListBuffer.get(plistBytes);
        InputStream is = new ByteArrayInputStream(plistBytes);
        Document d = builder.parse(is);
        return d;
    }

    /*
    Get values based on their key

    Examples:
        ...
        <key>CFName</key>
        <string>Protective Master Boot Record (MBR : 0)</string>
        ...


                ...
        <key>Data</key>
        <string>   base64 Mish block data here   </string>
        ...
    */
    private List<String> getDataByKeyName(Document d, String keyName) {
        NodeList keys = d.getElementsByTagName("key");
        List<String> data = new ArrayList<>();
        for (int i = 0; i < keys.getLength(); i++) {

            Node item = keys.item(i);
            if (item.getTextContent().equals(keyName)) {
                // For some reason, key data is two nodes after the key... we'd think it would be the next immediate sibling.
                // This is likely a Java XML Document issue?
                Node desiredNode = item.getNextSibling().getNextSibling();
                String textContent = desiredNode.getTextContent();
                data.add(textContent);
            }
        }

        return data;
    }

    /**
     * Creates a new decompressed output folder. The folder is deleted and re-created if it already exists.
     *
     * @return decompressed output folder reference
     */
    private File createDecompressedOutputFolder() {
        File decompressedOutputFolder = new File("./temp");
        if (decompressedOutputFolder.exists()) {
            decompressedOutputFolder.delete();
        }
        decompressedOutputFolder.mkdir();
        return decompressedOutputFolder;
    }
}