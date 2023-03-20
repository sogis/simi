package ch.so.agi.simi.core.dependency;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@MetaClass(name = "simiDependency_Dependency")
public class DependencyDto extends BaseUuidEntity {
    public static final String NAME = "simiDependency_DependencyRes";

    private static final long serialVersionUID = -8171356992105401614L;

    /**
     * Display string for the dependent Object.
     */
    @MetaProperty(mandatory = true)
    @NotNull
    @Length(message = "Must not be empty", min = 1)
    private String display;

    /**
     * Display string for the upstream dependency of the object.
     */
    @MetaProperty(mandatory = true)
    @NotNull
    @Length(message = "Must not be empty", min = 1)
    private String upstreamDisplay;

    /**
     * String displaying the dependency using the types. Example: View (Raster) -> Tabelle
     */
    @NotNull
    @MetaProperty(mandatory = true)
    @Length(message = "Must not be empty", min = 1)
    private String dependency;

    /**
     * Name of the root object (the query origin) of this dependency.
     */
    @MetaProperty(mandatory = true)
    @NotNull
    @Length(message = "Must not be empty", min = 1)
    private String rootObjName;

    @Override
    public String toString() {
        return "DependencyRes{" +
                "display='" + display + '\'' +
                ", upstreamDisplay='" + upstreamDisplay + '\'' +
                ", dependency='" + dependency + '\'' +
                '}';
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getUpstreamDisplay() {
        return upstreamDisplay;
    }

    public void setUpstreamDisplay(String upstreamDisplay) {
        this.upstreamDisplay = upstreamDisplay;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getRootObjName() {
        return rootObjName;
    }

    public void setRootObjName(String rootObjName) {
        this.rootObjName = rootObjName;
    }
}