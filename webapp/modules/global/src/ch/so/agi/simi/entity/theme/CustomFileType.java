package ch.so.agi.simi.entity.theme;

import ch.so.agi.simi.entity.SimiEntity;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMITHEME_FILE_TYPE")
@Entity(name = FileType.NAME)
@NamePattern("%s|name")
public class FileType extends SimiEntity {
    public static final String NAME = "simiTheme_FileType";

    private static final long serialVersionUID = 2540356321218561635L;


    @NotNull
    @Column(name = "MIME_TYPE", nullable = false, unique = true)
    private String mimeType;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "KUERZEL", unique = true, length = 50)
    private String kuerzel;

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}