package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.entity.theme.Theme;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.global.exc.CodedException;
import com.haulmont.cuba.core.global.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Optional;

public class ThemePubLoader {

    private static Logger log = LoggerFactory.getLogger(ThemePubLoader.class);

    private DataManager dataManager;

    public ThemePubLoader(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public ThemePublication byIdentifier(String identifier){
        Theme theme = loadTheme(identifier);
        ThemePublication themePub = loadThemePub(identifier, theme);

        return themePub;
    }

    private Theme loadTheme(String themePubIdentifier){
        Theme res = null;

        String query = MessageFormat.format("select e from {0} e where e.identifier = :identifier", Theme.NAME);

        Optional<Theme> themeWithThemePubIdentifier = dataManager.load(Theme.class)
                .query(query)
                .parameter("identifier", themePubIdentifier)
                .optional();

        if(themeWithThemePubIdentifier.isPresent()){
            res = themeWithThemePubIdentifier.get();
            log.debug("Found theme with identifier {} (same as themepub identifier)", themePubIdentifier);
        }
        else{ // query with omitted suffix
            String themeIdent = splitOnLastDot(themePubIdentifier, true);

            Optional<Theme> themeWithShortenedIdentifier = dataManager.load(Theme.class)
                    .query(query)
                    .parameter("identifier", themeIdent)
                    .optional();

            log.debug("Query with shortened identifier {} yielded {}", themeIdent, themeWithShortenedIdentifier.orElse(null));

            res = themeWithShortenedIdentifier.orElse(null);
        }

        if(res == null)
            throw new CodedException(402, CodedException.ERR_THEMEPUB_UNKNOWN, "Could not find theme for given themepub identifier");

        return res;
    }

    private ThemePublication loadThemePub(String themePubIdentifier, Theme parent){

        ThemePublication res = null;

        String themePubSuffix = null;
        if(!themePubIdentifier.equals(parent.getIdentifier()))
            themePubSuffix = splitOnLastDot(themePubIdentifier, false);

        String query = MessageFormat.format("select e from {0} e where e.theme = :theme and e.classSuffixOverride = :identSuffix", ThemePublication.NAME);
        Optional<ThemePublication> resWithSuffix = dataManager.load(ThemePublication.class)
                .query(query)
                .parameter("theme", parent)
                .parameter("identSuffix", themePubSuffix)
                .optional();

        log.debug("Load with parent {} and suffix {} yielded {}", parent.getIdentifier(), themePubSuffix, resWithSuffix.orElse(null));

        res = resWithSuffix.orElse(null);

        if(res == null) {
            throw new CodedException(404, CodedException.ERR_THEMEPUB_UNKNOWN,
                    MessageFormat.format("Could not find themepublication {0} in db", themePubIdentifier));
        }

        return res;
    }

    private static String splitOnLastDot(String wholeIdentifier, boolean returnLeftPart){
        String res = null;

        int dotIndex = wholeIdentifier.lastIndexOf(".");

        if(returnLeftPart)
            res = wholeIdentifier.substring(0, dotIndex);
        else
            res = wholeIdentifier.substring(dotIndex+1);

        return res;
    }
}
