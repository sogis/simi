package ch.so.agi.simi.core.service.pub;

import ch.so.agi.meta2file.libmain.Meta2Html;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.global.exc.CodedException;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
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

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager manager = persistence.getEntityManager();

            Connection con = manager.getConnection();

            UUID themePubId = queryUuid(themePubIdent);

            if(themePubId != null) {
                res = Meta2Html.renderDataSheet(themePubId, con);
            }
            else{
                res = "<!DOCTYPE html>\n" +
                "<html lang=\"de\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Themenbereitstellung nicht konfiguriert</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <p>Datenblatt kann nicht abgefragt werden, da die Themenbereitstellung " + themePubIdent + " in den Metadaten nicht konfiguriert ist.</p>\n" +
                "  </body>\n" +
                "</html>";
            }
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
        ThemePublication tp = ThemePubLoader.byIdentifier(themePubIdent);
        if(tp == null)
            return null;

        return tp.getId();
    }
}