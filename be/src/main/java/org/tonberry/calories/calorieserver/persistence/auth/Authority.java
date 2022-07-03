package org.tonberry.calories.calorieserver.persistence.auth;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@Builder(setterPrefix = "with")
@Table("auth.authorities")
public class Authority implements Serializable {
    @Id
    private long authorityId;

    private String name;
}
