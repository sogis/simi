package ch.so.agi.simi.web.beans.style;

import ch.so.agi.simi.web.beans.style.StyleStorageBean;

/**
 * DTO for the Content of a qml or zip style file
 */
public class StyleFileContent {

    private byte[] data;

    private StyleStorageBean.FileContentType fileContentType;

    public StyleFileContent(byte[] data, StyleStorageBean.FileContentType fileContentType){
        this.data = data;
        this.fileContentType = fileContentType;
    }

    public StyleStorageBean.FileContentType getFileContentType() {
        return fileContentType;
    }

    public byte[] getData() {
        return data;
    }
}
