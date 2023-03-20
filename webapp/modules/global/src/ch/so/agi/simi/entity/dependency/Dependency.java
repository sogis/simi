package ch.so.agi.simi.entity.dependency;

import com.haulmont.cuba.core.global.DbView;

import javax.persistence.*;

@DbView
@Table(name = Dependency.TABLE_NAME)
@Entity(name = Dependency.NAME)
public class Dependency extends DependencyBase {
    private static final long serialVersionUID = -8171356992105401614L;

    public static final String NAME = "simiDependency_Dependency";

    public static final String TABLE_NAME = "APP_DEPENDENCIES_V";
}