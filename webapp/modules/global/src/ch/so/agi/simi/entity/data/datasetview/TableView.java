package ch.so.agi.simi.entity.data.datasetview;

import ch.so.agi.simi.entity.data.PostgresTable;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIDATA_TABLE_VIEW")
@Entity(name = TableView.NAME)
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("#concatName|derivedIdentifier,title") //needed to define minimal view
public class TableView extends DataSetView {

    public static final String NAME = "simiData_TableView";

    private static final long serialVersionUID = -4901858225372396346L;

    @Composition
    @OneToMany(mappedBy = "tableView")
    @OrderBy("sort")
    private List<ViewField> viewFields;

    @Lob
    @Column(name = "FORM_JSON")
    private String formJson;

    @Lob
    @Column(name = "FORM_QGS")
    private String formQgs;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POSTGRES_TABLE_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private PostgresTable postgresTable;

    @Column(name = "ROW_FILTER_VIEW_NAME", length = 100)
    private String rowFilterViewName;

    @NotNull
    @Column(name = "SEARCH_TYPE", nullable = false)
    private String searchType;

    @Column(name = "SEARCH_FACET", length = 100)
    private String searchFacet;

    @Column(name = "SEARCH_FILTER_WORD", length = 100)
    private String searchFilterWord;

    public String getFormQgs() {
        return formQgs;
    }

    public void setFormQgs(String formQgs) {
        this.formQgs = formQgs;
    }

    public String getFormJson() {
        return formJson;
    }

    public void setFormJson(String formJson) {
        this.formJson = formJson;
    }

    @Override
    protected String typeAbbreviation(){
        return "View (Tabelle)";
    }

    public String getRowFilterViewName() {
        return rowFilterViewName;
    }

    public void setRowFilterViewName(String rowFilterViewName) {
        this.rowFilterViewName = rowFilterViewName;
    }

    public TableView_SearchTypeEnum getSearchType() {
        return searchType == null ? null : TableView_SearchTypeEnum.fromId(searchType);
    }

    public void setSearchType(TableView_SearchTypeEnum searchType) {
        this.searchType = searchType == null ? null : searchType.getId();
    }

    public String getSearchFilterWord() {
        return searchFilterWord;
    }

    public void setSearchFilterWord(String searchFilterWord) {
        this.searchFilterWord = searchFilterWord;
    }

    public String getSearchFacet() {
        return searchFacet;
    }

    public void setSearchFacet(String searchFacet) {
        this.searchFacet = searchFacet;
    }

    public List<ViewField> getViewFields() {
        return viewFields;
    }

    public void setViewFields(List<ViewField> viewFields) {
        this.viewFields = viewFields;
    }

    public PostgresTable getPostgresTable() {
        return postgresTable;
    }

    public void setPostgresTable(PostgresTable postgresTable) {
        this.postgresTable = postgresTable;
    }

    @PostConstruct
    public void postConstruct() {
        setSearchType(TableView_SearchTypeEnum.NO_SEARCH);
    }
}