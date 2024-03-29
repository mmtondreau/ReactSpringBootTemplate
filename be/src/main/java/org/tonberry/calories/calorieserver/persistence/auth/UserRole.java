package org.tonberry.calories.calorieserver.persistence.auth;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Getter
@Setter
@Table("auth.user_roles")
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements Serializable {
    @Id
    @Column("user_role_id")
    private long userRoleId;
    @Column("user_id")
    private long userId;
    @Column("role_id")
    private long roleId;
}
