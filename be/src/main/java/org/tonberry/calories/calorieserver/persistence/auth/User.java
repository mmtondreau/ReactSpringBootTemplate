package org.tonberry.calories.calorieserver.persistence.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "auth")
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password_enc")
    private String password;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<UserRole> userRoles;
    @Transient
    private List<SimpleGrantedAuthority> authorities;

    public List<UserRole> getUserRoles() {
        if (userRoles == null) {
            userRoles = new ArrayList<>();
        }
        return userRoles;
    }

    @Transient
    public List<Role> getRoles() {
        return getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toList());
    }

    @Transient
    public User addUserRole(UserRole userRole) {
        userRole.setUser(this);
        this.getUserRoles().add(userRole);
        return this;
    }

    @PostLoad
    public void postLoad() {
        authorities = getRoles().stream().map(Role::getRoleAuthorities).flatMap(Collection::stream)
                .map(rc -> new SimpleGrantedAuthority(rc.getAuthority().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
