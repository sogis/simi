package ch.so.agi.simi.core.morph;

import java.util.UUID;

public interface MorphProductService {
    String NAME = "simi_MorphProductService";

    UUID morphProduct(UUID prodID, ProdType fromType, ProdType toType) throws MorphException; // Gibt ID des umgewandelten Objektes zurück (ID kann aendern)
}