package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.global.exc.CodedException;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Connection;
import java.util.UUID;

@Service(GenerateThemePubDocService.NAME)
public class GenerateThemePubDocServiceBean implements GenerateThemePubDocService {

    @Inject
    private Persistence persistence;

    @Inject
    private DataManager dataManager;

    @Override
    public String generateDoc(String themePubIdent) throws CodedException {
        String res = null;

        try(Connection con = persistence.getEntityManager().getConnection()){
            UUID themePubId = queryUuid(themePubIdent);
            //res = Meta2Html.renderDataSheet(themePubId, con);
            res = "jar hell";
        }
        catch (CodedException ce){
            throw ce;
        }
        catch (Exception e){
            throw new CodedException(500, CodedException.ERR_SERVER, "", e);
        }

        return res;
    }

    private UUID queryUuid(String themePubIdent){
        ThemePublication tp = new ThemePubLoader(dataManager).byIdentifier(themePubIdent);
        return tp.getId();
    }
}