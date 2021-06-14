package ch.so.agi.simi.core.dependency;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@MetaClass(name = "simi_DependencyInfo")
public class DependencyInfo extends BaseUuidEntity {
    private static final long serialVersionUID = 3108423063171093049L;

    /**
     * Name of the dependent Object. Examples: ch.so.agi.MyFacadelayer, agi_mopublic_pub/adressen.sql
     */
    @MetaProperty(mandatory = true)
    @NotNull
    @Length(message = "Must not be empty", min = 1)
    private String objectName;

    /**
     * Sentence explaining the Relation of the returned object to the queried object (object in the request)
     */
    @MetaProperty(mandatory = true)
    @NotNull
    @Length(message = "Must not be empty", min = 1)
    private String dependencyInfo;

    /**
     * Type of the returned object. Examples: Product.FacadeLayer, Gretl.File, Component
     */
    @NotNull
    @MetaProperty(mandatory = true)
    @Length(message = "Must not be empty", min = 1)
    private String objectType;

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getDependencyInfo() {
        return dependencyInfo;
    }

    public void setDependencyInfo(String dependencyInfo) {
        this.dependencyInfo = dependencyInfo;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}