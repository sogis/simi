package ch.so.agi.simi.web.beans.filetransfer;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StyleStorageBeanTest {

    private static final int[] QGIS_DEFAULT_VERSION = new int[]{2,18}; // Default qgis version for the testing

    @Test
    public void uploadQmlOnly_Success(){
        StyleStorageBean trafo = new StyleStorageBean();
        StyleDbContent dbContent = trafo.transformFileToFields(createQmlContent(QGIS_DEFAULT_VERSION), QGIS_DEFAULT_VERSION);

        assertTrue(
                dbContent.getQmlContent().contains("<qgis"),
                "db qml content must contain therm '<qgis'"
        );
    }

    @Test
    public void uploadQmlOnly_Exception_TooYoungMajorVersion(){

        StyleStorageBean trafo = new StyleStorageBean();

        Exception ex = Assertions.assertThrows(RuntimeException.class, () -> {
            trafo.transformFileToFields(createQmlContent(new int[]{3,0}), QGIS_DEFAULT_VERSION);
        });

        assertTrue(
                ex.getMessage().contains("3.0"),
                "Errror message is expected to contain the given version number"
        );
    }


    @Test
    public void uploadQmlOnly_Exception_TooYongMinorVersion(){

        StyleStorageBean trafo = new StyleStorageBean();

        Exception ex = Assertions.assertThrows(RuntimeException.class, () -> {
            trafo.transformFileToFields(createQmlContent(new int[]{2,19}), QGIS_DEFAULT_VERSION);
        });

        assertTrue(
                ex.getMessage().contains("2.19"),
                "Errror message is expected to contain the given version number"
        );
    }

    @Test
    public void uploadZip_Success(){
        StyleStorageBean trafo = new StyleStorageBean();
        StyleDbContent dbContent = trafo.transformFileToFields(
                createZipFileContent(new String[]{"fuu.png", "bar.qml", "buz.png"}, 100),
                QGIS_DEFAULT_VERSION
        );

        assertTrue(
                dbContent.getQmlContent().contains("<qgis"),
                "db qml content must contain therm '<qgis'"
        );

        assertTrue(
                dbContent.getAssets().isPresent(),
                "Assets must be present"
        );
    }

    @Test
    public void uploadZip_NoQml_Exception(){

        StyleStorageBean trafo = new StyleStorageBean();

        Assertions.assertThrows(RuntimeException.class, () -> {
            trafo.transformFileToFields(
                    createZipFileContent(new String[]{"fuu.png", "bar.png", "buz.png"}, 100),
                    QGIS_DEFAULT_VERSION);
        });
    }

    @Test
    public void uploadZip_NoAssets_Exception(){

        StyleStorageBean trafo = new StyleStorageBean();

        Assertions.assertThrows(RuntimeException.class, () -> {
            trafo.transformFileToFields(
                    createZipFileContent(new String[]{"fuu.qml"}, 100),
                    QGIS_DEFAULT_VERSION);
        });
    }

    @Test
    public void downloadWithoutAssets_IsQmlFile(){

        StyleStorageBean trafo = new StyleStorageBean();
        StyleDbContent dbContent = new StyleDbContent(createQmlXmlString(QGIS_DEFAULT_VERSION), null);

        StyleFileContent fileContent = trafo.transformFieldsToFileContent(dbContent);

        assertTrue(
                fileContent.getFileContentType() == StyleStorageBean.FileContentType.QML,
                "Resulting file content must be of type qml if no assets are present"
        );

        assertTrue(
                trafo.transformFileToFields(fileContent, QGIS_DEFAULT_VERSION).getQmlContent().contains("<qgis"),
                "qml content string must contain \"<qgis\""
        );
    }

    @Test
    public void downloadWithAssets_IsZipFile(){
        StyleStorageBean trafo = new StyleStorageBean();
        StyleDbContent dbContent = createZipDbContent(new String[]{"fuu.png", "bar.png", "finally.qml"}, 100);

        StyleFileContent fileContent = trafo.transformFieldsToFileContent(dbContent);

        assertTrue(
                fileContent.getFileContentType() == StyleStorageBean.FileContentType.ZIP,
                "Resulting file content must be of type qml if no assets are present"
        );

        assertTrue(
                trafo.transformFileToFields(fileContent, QGIS_DEFAULT_VERSION).getQmlContent().contains("<qgis"),
                "qml content string must contain \"<qgis\""
        );
    }

    @Test
    public void encodeDecodeFatStyle_Success(){
        StyleDbContent dbContent = createZipDbContent(new String[]{"fuu.png", "bar.png", "finally.qml"}, 20000);

        StyleStorageBean trafo = new StyleStorageBean();
        StyleFileContent fileContent = trafo.transformFieldsToFileContent(dbContent);

        StyleDbContent dbContent2 = trafo.transformFileToFields(fileContent, QGIS_DEFAULT_VERSION);

        assertTrue(
                dbContent2.getQmlContent().contains("<qgis"),
                "qml content string must contain \"<qgis\""
        );
    }

    private String createQmlXmlString(int[] qmlVersion){
        return createQmlXmlString(qmlVersion, 0);
    }

    private String createQmlXmlString(int[] qmlVersion, int numFillCharacters){

        String fillString = RandomStringUtils.random(numFillCharacters, true, true);
        if(fillString == null)
            fillString = "";

        String fakeQml = MessageFormat.format(
                "<!DOCTYPE qgis PUBLIC \"http://mrcc.com/qgis.dtd\" \"SYSTEM\"><qgis version=\"{0}.{1}.17\">{2}</qgis>",
                qmlVersion[0],
                qmlVersion[1],
                fillString
                );

        return fakeQml;
    }

    private StyleFileContent createQmlContent(int[] qmlVersion) {
        return createQmlContent(qmlVersion, 100);
    }

    private StyleFileContent createQmlContent(int[] qmlVersion, int numFillCharacters){

        String fakeQml = createQmlXmlString(qmlVersion, numFillCharacters);

        return new StyleFileContent(
                fakeQml.getBytes(),
                StyleStorageBean.FileContentType.QML
                );
    }

    private StyleDbContent createZipDbContent(String[] fileNamesWithPaths, int maxAssetSize){

        String qmlContent = createQmlXmlString(QGIS_DEFAULT_VERSION, maxAssetSize);
        HashMap<String, byte[]> assets = new HashMap<>();

        Random random = new Random();

        boolean qmlIncluded = false;

        for (String fileName : fileNamesWithPaths) {
            byte[] bytes = new byte[random.nextInt(maxAssetSize)];
            random.nextBytes(bytes);

            assets.put(fileName, bytes);

            if(fileName.endsWith("qml"))
                qmlIncluded = true;
        }

        if(!qmlIncluded)
            throw new IllegalArgumentException("One of the given file names must be the qml file");

        return new StyleDbContent(qmlContent, assets);
    }

    /**
     * Creates a zip file content with the given full file names (path and name).
     * maxAssetSize is the maximum size of the randomly generated byte[] of one asset
     */
    private StyleFileContent createZipFileContent(String[] fileNamesWithPaths, int maxAssetSize) {

        byte[] zipBytes = null;

        StyleDbContent styleDbContent = createZipDbContent(fileNamesWithPaths, maxAssetSize);

        HashMap<String, byte[]> allFiles = styleDbContent.getAssets().get();
        byte[] qmlBytes = createQmlContent(QGIS_DEFAULT_VERSION).getData();
        allFiles.put("qml.qml", qmlBytes);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);

        try {
            for (String fileName : allFiles.keySet()) {
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOut.putNextEntry(zipEntry);

                zipOut.write(allFiles.get(fileName));
            }

            zipOut.close();

            zipBytes = bos.toByteArray();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return new StyleFileContent(zipBytes, StyleStorageBean.FileContentType.ZIP);
    }
}
