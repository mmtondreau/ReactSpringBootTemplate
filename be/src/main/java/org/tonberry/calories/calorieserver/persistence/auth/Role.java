package org.tonberry.calories.calorieserver.persistence.auth;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@Entity
@Table(name = "roles", schema = "auth")
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
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

    @Transient
    public List<String> getAuthorities() {
        return getRoleAuthorities().stream()
                .map(ra -> ra.getAuthority().getName())
                .collect(toList());
    }
}
