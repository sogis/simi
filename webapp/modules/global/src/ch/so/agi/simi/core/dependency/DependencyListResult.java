package ch.so.agi.simi.core.dependency;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class DependencyListResult implements Serializable {
    @NotNull
    private List<DependencyDto> dependencies;
    @NotNull
    private List<String> messages;

    public DependencyListResult(List<DependencyDto> dependencies, List<String> messages) {
        this.dependencies = dependencies;
        if(this.dependencies == null)
            this.dependencies = new LinkedList<>();

        this.messages = messages;
        if(this.messages == null)
            this.messages = new LinkedList<>();
    }

    public List<DependencyDto> getDependencies() {
        return dependencies;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void merge(DependencyListResult other){
        this.dependencies.addAll(other.getDependencies());
        this.messages.addAll(other.getMessages());
    }
}
