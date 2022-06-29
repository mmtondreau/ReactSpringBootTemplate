package org.tonberry.calories.calorieserver.persistence.auth;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user_roles", schema = "auth")
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private long userRoleId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
