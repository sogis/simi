package ch.so.agi.simi.entity.dependency;

import com.haulmont.cuba.core.entity.BaseIntegerIdEntity;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@MappedSuperclass
public class DependencyBase extends BaseIntegerIdEntity implements Comparable<DependencyBase> {

    private static final long serialVersionUID = -5504422511144589305L;

    @Lob
    @Column(name = "obj_name", nullable = false)
    @NotNull
    private String objName;

    @NotNull
    @Column(name = "level_num", nullable = false)
    private Integer levelNum;

    @Lob
    @Column(name = "type_name", nullable = false)
    @NotNull
    private String typeName;

    @Column(name = "upstream_id", nullable = false)
    @NotNull
    private UUID upstreamId;

    @Column(name = "obj_id", nullable = false)
    @NotNull
    private UUID objId;

    @Lob
    @Column(name = "qual_table_name", nullable = false)
    private String qualTableName;

    @Transient
    private String rootObjName;

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public UUID getUpstreamId() {
        return upstreamId;
    }

    public void setUpstreamId(UUID upstreamId) {
        this.upstreamId = upstreamId;
    }

    public UUID getObjId() {
        return objId;
    }

    public void setObjId(UUID objId) {
        this.objId = objId;
    }

    public String getQualTableName() {
        return qualTableName;
    }

    public void setQualTableName(String qualTableName) {
        this.qualTableName = qualTableName;
    }

    public String getRootObjName() {
        return rootObjName;
    }

    public void setRootObjName(String rootObjName) {
        this.rootObjName = rootObjName;
    }

    @Override
    public int compareTo(DependencyBase dependency) {
        if(dependency == this)
            return 0;

        int res = this.getLevelNum().compareTo(dependency.getLevelNum());

        if(res == 0) //same level
            res = this.getObjName().compareTo(dependency.getObjName());

        return res;
    }
}