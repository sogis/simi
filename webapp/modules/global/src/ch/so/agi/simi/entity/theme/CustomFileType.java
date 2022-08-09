package ch.so.agi.simi.entity.theme;

import ch.so.agi.simi.entity.SimiEntity;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMITHEME_FILE_TYPE")
@Entity(name = CustomFileType.NAME)
@NamePattern("%s|name")
public class CustomFileType extends SimiEntity {
    public static final String NAME = "simiTheme_CustomFileType";

    private static final long serialVersionUID = 2540356321218561635L;


    @NotNull
    @Column(name = "MIME_TYPE", nullable = false, unique = true)
    private String mimeType;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "KUERZEL", nullable = false, unique = true, length = 50)
    @NotNull
    private String kuerzel;

    @JoinTable(name = "SIMITHEME_THEME_PUBLICATION_CUSTOM_FILE_TYPE_LINK",
            joinColumns = @JoinColumn(name = "CUSTOM_FILE_TYPE_ID"),
            inverseJoinColumns = @JoinColumn(name = "THEME_PUBLICATION_ID"))
    @ManyToMany
    private List<ThemePublication> themePublications;

    public List<ThemePublication> getThemePublications() {
        return themePublications;
    }

    public void setThemePublications(List<ThemePublication> themePublications) {
        this.themePublications = themePublications;
    }

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