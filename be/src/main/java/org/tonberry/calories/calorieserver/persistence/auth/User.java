package org.tonberry.calories.calorieserver.persistence.auth;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Table("auth.users")
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, Serializable {
    @Id
    @Column("user_id")
    private long userId;

    @Column("username")
    private String username;

    @Column("password_enc")
    private String password;

    @Column("account_non_expired")
    private boolean accountNonExpired;

    @Column("account_non_locked")
    private boolean accountNonLocked;

    @Column("credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column("enabled")
    private boolean enabled;

    @Transient
    private List<SimpleGrantedAuthority> authorities;

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Transient
    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /**
     * @Transient public Set<UserRole> getUserRoles() {
     * if (userRoles == null) {
     * userRoles = new HashSet<>();
     * }
     * return userRoles;
     * }
     * @Transient public List<Role> getRoles() {
     * return getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toList());
     * }
     * @Transient public User addUserRole(UserRole userRole) {
     * userRole.setUser(this);
     * this.getUserRoles().add(userRole);
     * return this;
     * }
     * <p>
     * /*
     * @PostLoad public void postLoad() {
     * authorities = getRoles().stream().map(Role::getRoleAuthorities).flatMap(Collection::stream)
     * .map(rc -> new SimpleGrantedAuthority(rc.getAuthority().getName()))
     * .collect(Collectors.toList());
     * }
     */

}
