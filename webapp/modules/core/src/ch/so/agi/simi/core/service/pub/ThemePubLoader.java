package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.global.exc.CodedException;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.util.UUID;

public class ThemePubLoader {

    private static Logger log = LoggerFactory.getLogger(ThemePubLoader.class);

    public static ThemePublication byIdentifier(String identifier){

        ThemePublication res = null;

        Persistence p = AppBeans.get(Persistence.class);

        try(Transaction t = p.createTransaction()){
            UUID tpId = queryIdNative(identifier, p.getEntityManager());

            log.debug("Load themepub by id {}", tpId);
            res = p.getEntityManager().find(ThemePublication.class, tpId);

            t.commit();
        }
        return res;
    }

    private static UUID queryIdNative(String identifier, EntityManager manager){
        UUID res = null;

        /*
        Explanation to concat(t.identifier, ('.' || tp.class_suffix_override)) when class_suffix_override is null:
            ('.' || tp.class_suffix_override) resolves to null if class_suffix_override is null
            concat(t.identifier, (null)) resolves to t.identifier
         */
        String select =
                "SELECT tp.id FROM  simi.simitheme_theme_publication tp JOIN simi.simitheme_theme t ON tp.theme_id = t.id"
                        + " WHERE concat(t.identifier, ('.' || tp.class_suffix_override)) = ?1";

        Query query = manager.createNativeQuery(select);
        query.setParameter(1, identifier);

        try{
            log.debug("Executing query: {}", query);
            res = (UUID) query.getSingleResult(); // throws jaxax.persistence.NoResultException
        }
        catch(NoResultException nores) {
            throw new CodedException(404, CodedException.ERR_THEMEPUB_UNKNOWN, nores.getMessage());
        }

        return res;
    }
}