package org.tonberry.calories.calorieserver.persistence.auth;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
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
    @Column("role_authority_id")
    private long roleAuthorityId;

    @Column("role_id")
    private long roleId;

    @Column("authority_id")
    private long authorityId;
}
