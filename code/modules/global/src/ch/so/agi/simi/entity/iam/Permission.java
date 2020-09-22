package ch.so.agi.simi.entity.iam;

import ch.so.agi.simi.entity.SimiStandardEntity;
import ch.so.agi.simi.entity.product.DataSetView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIIAM_PERMISSION", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMI_PERMISSION_UNQ_DATA_SET_VIEW_ID_ROLE_ID", columnNames = {"DATA_SET_VIEW_ID", "ROLE_ID"})
})
@Entity(name = "simiIAM_Permission")
public class Permission extends SimiStandardEntity {
    private static final long serialVersionUID = -5062553741213113449L;

    @NotNull
    @Column(name = "LEVEL_", nullable = false)
    private String level = PermissionLevelEnum.READ.getId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATA_SET_VIEW_ID")
    @NotNull
    private DataSetView dataSetView;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROLE_ID")
    @NotNull
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public DataSetView getDataSetView() {
        return dataSetView;
    }

    public void setDataSetView(DataSetView dataSetView) {
        this.dataSetView = dataSetView;
    }

    public PermissionLevelEnum getLevel() {
        return level == null ? null : PermissionLevelEnum.fromId(level);
    }

    public void setLevel(PermissionLevelEnum level) {
        this.level = level == null ? null : level.getId();
    }
}