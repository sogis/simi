package ch.so.agi.simi.web.beans.filetransfer;


import org.springframework.stereotype.Component;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

// 8:45 - 10:35 / 11:35 - 12:15 / 12.50 -17.25gigi

/*
Contains the logic to: ....
- check uploaded style files (including zipped with assets)
- transform between the storage in db fields and the file representation of the styles.
 */
@Component(StyleStorageBean.NAME)
public class StyleStorageBean {
    public static final String NAME = "simi_StyleStorageBean";

    /**
     * Transforms the uploaded style file content for storage in the corresponding db fields.
     *
     * <p>The content of the qml is returned in the first String in the return Pair.
     *
     * <p>If the given File is a zip with assets (and of course qml), the assets are
     * extracted into [filename]-[fileContent] key value pairs.
     */
    public StyleDbContent transformFileToFields(StyleFileContent styleContent, int[] maxQmlVersion){

        String qmlContent = null;
        HashMap<String, byte[]> assets = null;

        if(styleContent.getFileContentType() == FileContentType.QML) {
            qmlContent = qmlToXmlString(styleContent.getData(), maxQmlVersion);
        }
        else {
            StyleStorageBean.ZipContent filesInZip = extractFilesInZip(styleContent.getData());
            qmlContent = qmlToXmlString(filesInZip.getQmlContent(), maxQmlVersion);
            assets = filesInZip.getAssets();
        }

        return new StyleDbContent(qmlContent, assets);
    }

    private static StyleStorageBean.ZipContent extractFilesInZip(byte[] zipBytes){

        byte[] qml = null;
        HashMap<String, byte[]> assets = new HashMap<>();

        try {
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes));
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                if(zipEntry.isDirectory()) {
                    zipEntry = zis.getNextEntry();
                    continue;
                }

                byte[] entryBytes = zis.readAllBytes();

                if(zipEntry.getName().endsWith("qml")){
                    qml = entryBytes;
                }
                else { // zip
                    assets.put(zipEntry.getName(), entryBytes);
                }

                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

        return new StyleStorageBean.ZipContent(qml, assets);
    }

    private static String qmlToXmlString(byte[] data, int[] maxQmlVersion){
        String qml = new String(data, StandardCharsets.UTF_8);

        assertQmlMaxVersion(qml, maxQmlVersion);

        return qml;
    }

    private static byte[] qmlToBytes(String qmlXml){
        ByteBuffer buf = StandardCharsets.UTF_8.encode(qmlXml);
        return buf.array();
    }

    private static void assertQmlMaxVersion(String qmlContent, int[] maxQmlVersion){

        String version = null;

        int maxMajor = maxQmlVersion[0];
        int maxMinor = maxQmlVersion[1];

        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new StringReader(qmlContent));

            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();

                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("qgis")) {
                        version = startElement.getAttributeByName(new QName("version")).getValue();
                    }

                    break; // we are not interested in rest of qml file....
                }
            }

            reader.close();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

        String[] parts = version.split("\\.");

        if(maxMajor < Integer.parseInt(parts[0]) || maxMinor < Integer.parseInt(parts[1])) {
            throw new RuntimeException(MessageFormat.format(
                    "QGIS qml major version must not be higher than {0}.{1}, but version of given qml file is {2}",
                    maxMajor,
                    maxMinor,
                    version
            ));
        }
    }


    /**
     * Transforms a style back to a zip or qml file, depending on whether assets are present.
     * @param styleDbContent: The style representation in the db.
     * @return The qml or zip file content.
     */
    public StyleFileContent transformFieldsToFileContent(StyleDbContent styleDbContent){

        byte[] fileContent = null;
        FileContentType contentType = null;

        if(styleDbContent.getAssets().isPresent()){
            contentType = FileContentType.ZIP;
            fileContent = encodeZip(styleDbContent);
        }
        else {
            contentType = FileContentType.QML;
            fileContent = qmlToBytes(styleDbContent.getQmlContent());
        }

        return new StyleFileContent(fileContent, contentType);
    }

    private static byte[] encodeZip(StyleDbContent styleDbContent){

        HashMap<String, byte[]> files = styleDbContent.getAssets().get();

        byte[] qmlBytes = qmlToBytes(styleDbContent.getQmlContent());
        files.put("qml.qml", qmlBytes);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);

        try {
            for (String fileName : files.keySet()) {
                byte[] fileData = files.get(fileName);

                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOut.putNextEntry(zipEntry);

                zipOut.write(fileData);
            }
            zipOut.close();
        }
        catch (IOException ioe){
            throw new RuntimeException("Could not encode file contents to zip", ioe);
        }

        return bos.toByteArray();
    }

    public enum FileContentType {
        ZIP, QML;

        public static FileContentType forFileName(String fileName){
            String lower = fileName.toLowerCase();

            for (FileContentType type : FileContentType.values()){
                if(fileName.toLowerCase().endsWith(type.name().toLowerCase()))
                    return type;
            }

            throw new RuntimeException("Could not determine style file type. File suffix must be either qml or zip");
        }

        public String asFileSuffix(){
            return "." + name().toLowerCase();
        }
    }

    /**
     * Internal DTO for the content of a style zip with one
     * qml and 1-n assets with filename and content as byte[]
     */
    private static class ZipContent{

        private byte[] qmlContent;
        private HashMap<String, byte[]> assets;

        ZipContent(byte[] qmlContent, HashMap<String, byte[]> assets){

            if(qmlContent == null)
                throw new IllegalArgumentException("qmlContent must not be null");

            if(assets == null || assets.size() == 0)
                throw new IllegalArgumentException("assets must not be null or empty");

            this.qmlContent = qmlContent;
            this.assets = assets;
        }

        public byte[] getQmlContent() {
            return qmlContent;
        }

        public HashMap<String, byte[]> getAssets() {
            return assets;
        }
    }
}

