package ch.so.agi.simi.web;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.screen.ScreenContext;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

@Component(StyleUploadDownloadBean_.NAME)
public class StyleUploadDownloadBean_ {
    public static final String NAME = "simi_StyleUploadDownloadBean";

    public static final int TARGET_MAJOR_VERSION = 2;
    public static final int TARGET_MINOR_VERSION = 18;

    public void downloadString(String content, String filename) {
        if (content != null) {
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

            AppConfig.createExportDisplay(null).show(new ByteArrayDataProvider(bytes), filename);
        }
    }

    public static class StyleUploadException extends Exception {
        public StyleUploadException(String errorMessage) {
            super(errorMessage);
        }

        public StyleUploadException(String errorMessage, Throwable cause) {
            super(errorMessage, cause);
        }
    }

    /**
     * Check the Version of the uploaded file and return the file content as a String. If the file fails the check an
     * exception is thrown.
     * @param inputStream The InputStream of the uploaded file
     * @throws StyleUploadException Something went wrong either with reading the file or the version does not match
     */
    public String checkUpload(InputStream inputStream) throws StyleUploadException {
        try {
            String fileContent = inputStreamToString(inputStream);

            String qgisVersionString = getQGISVersion(fileContent);

            String[] version = qgisVersionString.split("\\.");
            int major = Integer.parseInt(version[0]);
            int minor = Integer.parseInt(version[1]);

            if (major == TARGET_MAJOR_VERSION && minor == TARGET_MINOR_VERSION) {
                return fileContent;
            } else {
                throw new StyleUploadException("Ausgewälte qml Datei hat eine falsche Version: " + qgisVersionString + "\nErwartet wird version " + TARGET_MAJOR_VERSION + "." + TARGET_MINOR_VERSION);
            }
        } catch (XMLStreamException | IOException e) {
            throw new StyleUploadException("Beim Lesen der Datei ist ein fehler aufgetreten.", e);
        } catch (NumberFormatException e) {
            throw new StyleUploadException("Beim Überprüfen der Version ist ein Fehler aufgetreten.", e);
        }
    }

    /**
     * Extract the qgis version from a .qml string
     * @param content The content of the .qml file
     * @return Version string
     * @throws XMLStreamException
     * @throws StyleUploadException Unable to find the qgis version string
     */
    private static String getQGISVersion(String content) throws XMLStreamException, StyleUploadException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new StringReader(content));

        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("qgis")) {
                    return startElement.getAttributeByName(new QName("version")).getValue();
                }
            }
        }

        throw new StyleUploadException("Die Version der Datei konnte nicht gefunden werden.");
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        int charsRead;
        while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            out.append(buffer, 0, charsRead);
        }
        return out.toString();
    }
}