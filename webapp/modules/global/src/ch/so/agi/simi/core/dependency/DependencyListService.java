package ch.so.agi.simi.core.dependency;

import java.util.List;
import java.util.UUID;

public interface DependencyListService {
    String NAME = "simi_DependencyListService";

    List<DependencyDto> listDependenciesForObjs(List<UUID> objIds);
}