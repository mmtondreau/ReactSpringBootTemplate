package org.tonberry.calories.calorieserver.persistence.auth;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "role", schema = "auth")
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long roleId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleAuthorities> roleAuthorities;

    public List<RoleAuthorities> getRoleAuthorities() {
        if (roleAuthorities == null) {
            roleAuthorities = new ArrayList<>();
        }
        return roleAuthorities;
    }

    @Transient
    public Role addRoleAuthority(RoleAuthorities roleAuthorities) {
        roleAuthorities.setRole(this);
        this.getRoleAuthorities().add(roleAuthorities);
        return this;
    }
}
