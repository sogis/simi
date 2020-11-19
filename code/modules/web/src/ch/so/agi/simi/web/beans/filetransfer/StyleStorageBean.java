package ch.so.agi.simi.web.beans.filetransfer;


import org.springframework.stereotype.Component;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


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
     * <p>If the given File is a zip with assets, the assets are encoded as json key value pairs.
     * The key is the non qualified file name of the asset (a symbol png, ...). The value is the
     * base64 encoded String of the file content. XML asset files like svg's are also base64 encoded.
     *
     * @param styleContent The uploaded qml or zip File content
     * @return maxQmlVersion highest acceptable version. maxQmlVersion[0] = major version, maxQmlVersion[1] = minor version,
     */
    public StyleDbContent transformFileToFields(StyleFileContent styleContent, int[] maxQmlVersion){

        String qmlContent = null;
        String assetsContent = null;

        if(styleContent.getFileContentType() == FileContentType.QML) {
            qmlContent = qmlToXmlString(styleContent.getData(), maxQmlVersion);
        }
        else {
            StyleStorageBean.ZipContent filesInZip = extractFilesInZip(styleContent.getData());
            qmlContent = qmlToXmlString(filesInZip.getQmlContent(), maxQmlVersion);
            assetsContent = "fuu"; //encodeToJson(filesInZip.getAssets());
        }

        return new StyleDbContent(qmlContent, assetsContent);
    }

    private static StyleStorageBean.ZipContent extractFilesInZip(byte[] zipBytes){

        byte[] qml = null;
        Map<String, byte[]> assets = new Hashtable<>();

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
                }
            }
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
        return null;
    }

    public enum FileContentType {
        ZIP, QML
    }

    /**
     * DTO for the content of a style zip with one
     * qml and 1-n assets with
     */
    private static class ZipContent{

        private byte[] qmlContent;
        private Map<String, byte[]> assets;

        ZipContent(byte[] qmlContent, Map<String, byte[]> assets){

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

        public Map<String, byte[]> getAssets() {
            return assets;
        }
    }
}

