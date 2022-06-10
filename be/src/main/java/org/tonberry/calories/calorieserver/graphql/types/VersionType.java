package org.tonberry.calories.calorieserver.graphql.types;

import lombok.*;
import org.tonberry.calories.calorieserver.persistence.VersionEntity;

@Getter
@Setter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class VersionType {
    String name;
    Long id;

    public VersionType(VersionEntity ve) {
        id = ve.getVersion_id();
        name = ve.getName();
    }
}
