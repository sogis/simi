package ch.so.agi.simi.core.copy;

import java.util.UUID;

public interface CopyService {
    String NAME = "simi_CopyService";

    /**
     * Erstellt und persistiert eine Kopie des via ID identifizierten Dataproduct
     * @param id ID des zu kopierenden Dataproduct
     * @return ID der erstellten Kopie des Dataproduct
     */
    UUID copyProduct(UUID id);
}