package ch.so.agi.simi.web.screens.theme.pubsub;

import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiTheme_PublishedSubArea.browse")
@UiDescriptor("published-sub-area-browse.xml")
@LookupComponent("publishedSubAreasTable")
@LoadDataBeforeShow
public class PublishedSubAreaBrowse extends StandardLookup<PublishedSubArea> {
}