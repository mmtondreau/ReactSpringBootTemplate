package org.tonberry.calories.calorieserver.persistence.auth;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Table("auth.role_authorities")
public class RoleAuthority implements Serializable {
    @Id
    private long roleAuthorityId;

    private long roleId;
    private long authorityId;
}
