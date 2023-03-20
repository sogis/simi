package ch.so.agi.simi.entity.dependency;

import com.haulmont.cuba.core.global.DbView;

import javax.persistence.*;

@DbView
@Table(name = DependencyForTest.TABLE_NAME)
@Entity(name = DependencyForTest.NAME)
public class DependencyForTest extends DependencyBase {
    private static final long serialVersionUID = 6148236302104229526L;

    public static final String NAME = "simiTest_Dependency";

    public static final String TABLE_NAME = "SIMITEST_DEPENDENCY";
}