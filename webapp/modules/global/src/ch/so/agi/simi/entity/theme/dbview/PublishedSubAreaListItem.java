package ch.so.agi.simi.entity.theme.dbview;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.DbView;
import com.haulmont.cuba.core.global.DdlGeneration;

import javax.persistence.*;
import java.util.Date;

@DbView
@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.CREATE_ONLY)
@Table(name = "app_pubsubarea_v")
@Entity(name = "simiTheme_PublishedSubAreaListItem")
public class PublishedSubAreaListItem extends BaseUuidEntity {
    private static final long serialVersionUID = 5586829656819316003L;

    @Lob
    @Column(name = "tp_display")
    private String tpDisplay;

    @Lob
    @Column(name = "sub_display")
    private String subDisplay;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "published")
    private Date published;

    public String getTpDisplay() {
        return tpDisplay;
    }

    public void setTpDisplay(String tpDisplay) {
        this.tpDisplay = tpDisplay;
    }

    public String getSubDisplay() {
        return subDisplay;
    }

    public void setSubDisplay(String subDisplay) {
        this.subDisplay = subDisplay;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }
}