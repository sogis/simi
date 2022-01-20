package ch.so.agi.simi.core.morph;

import java.util.UUID;

public interface MorphProductService {
    String NAME = "simi_MorphProductService";

    /**
     * Wandelt ein Subtyp eines Dataproductes von einem Typ in einen anderen um. Beispielsweise Facadelayer --> Layergroup
     * @param prodID ID des umzuwandelnden Produktes
     * @param fromType Ausgangstyp (Wird übergeben, da dies Serverseitig extra ermittelt werden müsste, aber Clientseitig eh bekannt ist)
     * @param toType Zieltyp
     * @return Die ID des umgewandelten (und neu erstellten) Dataproduct
     * @throws MorphException
     */
    UUID morphProduct(UUID prodID, ProdType fromType, ProdType toType) throws MorphException; // Gibt ID des umgewandelten Objektes zurück (ID kann aendern)
}