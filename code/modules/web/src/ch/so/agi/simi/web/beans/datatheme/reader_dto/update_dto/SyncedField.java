package ch.so.agi.simi.web.beans.datatheme.reader_dto.update_dto;

import ch.so.agi.simi.entity.data.TableField;

public class SyncedField {

    private TableField tf;
    private FieldSyncState syncRes;

    public SyncedField(TableField tf, FieldSyncState syncRes){
        this.tf = tf;
        this.syncRes = syncRes;
    }

    public TableField getTableField() {
        return tf;
    }

    public void setTableField(TableField tf) {
        this.tf = tf;
    }

    public FieldSyncState getSyncState() {
        return syncRes;
    }

    public void setSyncState(FieldSyncState syncRes) {
        this.syncRes = syncRes;
    }


}
