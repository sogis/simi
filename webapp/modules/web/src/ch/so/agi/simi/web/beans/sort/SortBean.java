package ch.so.agi.simi.web.beans.sort;

import ch.so.agi.simi.entity.Sortable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component(SortBean.NAME)
public class SortBean {
    public static final String NAME = "simi_SortBean";

    /**
     * Adjust the sort property of {@link Sortable} Entities. The list is sorted by the existing sort value and then the sort
     * properties are changed so that they increments in steps of 10 starting with 0.
     *
     * @param entities The entities to modify.
     * @param <T> The type of the entities.
     * @return A List containing the modified entities.
     */
    public <T extends Sortable> List<T> AdjustSort(List<T> entities) {
        int i = 0;
        List<T> sortedEntities = entities.stream()
                .sorted(Comparator.comparing(Sortable::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        for (T item : sortedEntities) {
            // set new value
            item.setSort(i);
            i += 10;
        }

        return sortedEntities;
    }
}