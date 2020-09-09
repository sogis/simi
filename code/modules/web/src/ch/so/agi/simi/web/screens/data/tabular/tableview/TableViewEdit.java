package ch.so.agi.simi.web.screens.data.tabular.tableview;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import ch.so.agi.simi.entity.data.tabular.TableView;
import ch.so.agi.simi.entity.data.tabular.ViewField;
import ch.so.agi.simi.entity.product.DataSetView_SearchTypeEnum;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@UiController("simiData_TableView.edit")
@UiDescriptor("table-view-edit.xml")
@EditedEntityContainer("tableViewDc")
@LoadDataBeforeShow
public class TableViewEdit extends StandardEditor<TableView> {
    @Inject
    private CollectionLoader<DataProduct_PubScope> pubScopesDl;
    @Inject
    private InstanceContainer<TableView> tableViewDc;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Notifications notifications;
    @Inject
    private FileUploadField uploadStyleServerBtn;
    @Inject
    private FileUploadField uploadStyleDesktopBtn;
    @Inject
    private Dialogs dialogs;
    @Inject
    private TextField<String> searchFilterWordField;
    @Inject
    private Table<ViewField> viewFieldsTable;
    @Inject
    private CollectionPropertyContainer<ViewField> viewFieldsDc;

    @Subscribe
    public void onInitEntity(InitEntityEvent<TableView> event) {
        TableView tableView = event.getEntity();
        pubScopesDl.load();
        pubScopesDl.getContainer().getItems().stream()
            .filter(DataProduct_PubScope::getDefaultValue)
            .findFirst()
            .ifPresent(tableView::setPubScope);
    }

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        searchFilterWordField.addValidator(value -> {
            if (tableViewDc.getItem().getSearchType() != DataSetView_SearchTypeEnum.NEIN && (value == null || value.isEmpty()))
                throw  new ValidationException("Wenn Suchtyp 'Immer' oder 'falls geladen' ist, muss Filter Wort angegeben werden.");
        });
    }

    @Subscribe("downloadStyleServerBtn")
    public void onDownloadStyleServerBtnClick(Button.ClickEvent event) {
        TableView tableView = tableViewDc.getItem();
        downloadString(tableView.getStyleServer(), tableView.getIdentifier() + ".Server.qml");
    }

    @Subscribe("uploadStyleServerBtn")
    public void onUploadStyleServerBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        checkUpload(uploadStyleServerBtn, content -> tableViewDc.getItem().setStyleServer(content));
    }

    @Subscribe("downloadStyleDesktopBtn")
    public void onDownloadStyleDesktopBtnClick(Button.ClickEvent event) {
        TableView tableView = tableViewDc.getItem();
        downloadString(tableView.getStyleDesktop(), tableView.getIdentifier() + ".Desktop.qml");
    }

    @Subscribe("uploadStyleDesktopBtn")
    public void onUploadStyleDesktopBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        checkUpload(uploadStyleDesktopBtn, content -> tableViewDc.getItem().setStyleDesktop(content));
    }

    private void downloadString(String content, String filename) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        exportDisplay.show(new ByteArrayDataProvider(bytes), filename);
    }

    private void checkUpload(FileUploadField uploadField, Consumer<String> assignResult) {
        try {
            InputStream inputStream = uploadField.getFileContent();
            String fileContent = inputStreamToString(inputStream);

            String qgisVersionString = getQGISVersion(fileContent);

            String[] version = qgisVersionString.split("\\.");
            int major = Integer.parseInt(version[0]);
            int minor = Integer.parseInt(version[1]);

            if (major == 2 && minor == 18) {
                assignResult.accept(fileContent);

                notifications.create()
                        .withCaption(uploadField.getFileName() + " uploaded")
                        .show();
            } else {
                dialogs.createMessageDialog()
                        .withCaption("Upload")
                        .withMessage("Ausgewälte qml Datei hat eine falsche Version: " + qgisVersionString + "\nErwartet wird version 2.18")
                        .withType(Dialogs.MessageType.WARNING)
                        .show();
            }
        } catch (XMLStreamException | IOException e) {
            dialogs.createMessageDialog()
                    .withCaption("Upload")
                    .withMessage("Beim Lesen der Datei " + uploadField.getFileName() + " ist ein fehler aufgetreten.")
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
        }
    }

    /**
     * Extract the qgis version from a .qml string
     * @param content The content of the .qml file
     * @return Version string
     * @throws XMLStreamException
     */
    private static String getQGISVersion(String content) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new StringReader(content));

        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("qgis")) {
                    return startElement.getAttributeByName(new QName("version")).getValue();
                }
            }
        }
        return null;
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        int charsRead;
        while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            out.append(buffer, 0, charsRead);
        }
        return out.toString();
    }

    @Subscribe("viewFieldsTable.sortAction")
    public void onSingleActorsTableSortAction(Action.ActionPerformedEvent event) {
        viewFieldsTable.sort("sort", Table.SortDirection.ASCENDING);
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreCommit(DataContext.PreCommitEvent event) {
        int i = 0;
        List<ViewField> viewFields = viewFieldsDc.getItems().stream()
                .sorted(Comparator.comparing(ViewField::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        // go through the data container items. The same can be done using getEditedEntity().getSingleActorList().
        for (ViewField item : viewFields) {
            // set new value and add modified instance to the commit list
            item.setSort(i);
            event.getModifiedInstances().add(item);
            i += 10;
        }
    }
}
