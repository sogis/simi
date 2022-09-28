package ch.so.agi.simi.web.screens.theme.pubsub;

import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiTheme_PublishedSubArea.edit")
@UiDescriptor("published-sub-area-edit.xml")
@EditedEntityContainer("publishedSubAreaDc")
@LoadDataBeforeShow
public class PublishedSubAreaEdit extends StandardEditor<PublishedSubArea> {
}