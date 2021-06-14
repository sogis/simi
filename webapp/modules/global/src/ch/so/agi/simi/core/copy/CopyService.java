package ch.so.agi.simi.core.copy;

import java.util.UUID;

public interface CopyService {
    String NAME = "simi_CopyService";

    UUID copyProduct(UUID id);
}