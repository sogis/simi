package ch.so.agi.simi.entity;

import com.haulmont.cuba.core.entity.*;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@MappedSuperclass
public class SimiEntity extends BaseUuidEntity implements Versioned, Updatable, Creatable {
    private static final long serialVersionUID = -1260377763174168346L;

    @Column(name = "UPDATE_TS")
    private Date updateTs;

    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;

    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @Lob
    @Column(name = "EXT1")
    private String ext1;

    @Lob
    @Column(name = "EXT2")
    private String ext2;

    @Lob
    @Column(name = "EXT3")
    private String ext3;

    @Lob
    @Column(name = "EXT4")
    private String ext4;

    @Lob
    @Column(name = "EXT5")
    private String ext5;

    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public Date getUpdateTs() {
        return updateTs;
    }

    @Override
    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }
}