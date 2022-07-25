package ch.so.agi.simi.core.test;

import ch.so.agi.simi.entity.theme.Theme;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.ThemePublication_TypeEnum;
import ch.so.agi.simi.entity.theme.org.Agency;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.testsupport.TestContainer;

import java.util.Optional;

public class Util {

    private static final String THEMEPUB_CLASS_SUFFIX_OVERRIDE = "ch.so.agi.inttests.themePub";

    public static ThemePublication createThemePub(TestContainer container, DataManager dataManager){
        removeThemePubs(dataManager);

        Agency a = container.metadata().create(Agency.class);
        a.setName("copytest");
        a.setAbbreviation("test");
        a.setEmail("email");
        a.setPhone("phone");
        a.setUrl("url");

        Theme t = container.metadata().create(Theme.class);
        t.setIdentifier("copytest");
        t.setTitle("title");
        t.setDescription("desc");
        t.setDataOwner(a);

        ThemePublication p = container.metadata().create(ThemePublication.class);
        p.setDataClass(ThemePublication_TypeEnum.TABLE_SIMPLE);
        p.setTheme(t);
        p.setCoverageIdent("covIdent");
        p.setClassSuffixOverride(THEMEPUB_CLASS_SUFFIX_OVERRIDE);

        CommitContext c = new CommitContext();
        c.addInstanceToCommit(a);
        c.addInstanceToCommit(t);
        c.addInstanceToCommit(p);

        dataManager.commit(c);

        return p;
    }

    public static void removeThemePubs(DataManager dataManager){
        String viewName = "copy-dataProduct-themePub";
        Optional<ThemePublication> ot = dataManager.load(ThemePublication.class)
                .query("select e from simiTheme_ThemePublication e where e.classSuffixOverride like :para")
                .parameter("para", THEMEPUB_CLASS_SUFFIX_OVERRIDE)
                .view(viewName)
                .optional();

        if(!ot.isPresent())
            return;

        ThemePublication t = ot.get();

        dataManager.remove(t.getTheme());
        dataManager.remove(t.getTheme().getDataOwner());
    }
}
