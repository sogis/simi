package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.global.exc.CodedException;
import org.springframework.stereotype.Service;

@Service(GenerateThemePubDocService.NAME)
public class GenerateThemePubDocServiceBean implements GenerateThemePubDocService {

    @Override
    public String generateDoc(String themePubIdent) throws CodedException {
        return null;
    }
}