package ch.so.agi.simi.core.dependency;

import java.util.List;

public interface DependencyRootService {
    String NAME = "simi_DependencyRootService";

    List<DependencyRootDto> queryRoots();
}