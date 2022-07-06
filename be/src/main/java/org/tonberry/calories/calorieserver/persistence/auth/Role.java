package org.tonberry.calories.calorieserver.persistence.auth;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@Table("auth.roles")
@Builder(setterPrefix = "with")
public class Role implements Serializable {
    @Id
    @Column("role_id")
    private long roleId;

    private String name;

    //private Set<RoleAuthority> roleAuthorities = new HashSet<>();

    /*public Set<RoleAuthority> getRoleAuthorities() {
        if (roleAuthorities == null) {
            roleAuthorities = new HashSet<>();
        }
        return roleAuthorities;
    }*/
/*
    @Transient
    public Role addRoleAuthority(RoleAuthority roleAuthorities) {
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
    */

}
