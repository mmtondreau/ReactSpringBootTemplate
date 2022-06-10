package org.tonberry.calories.calorieserver.persistence.auth;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role_authorities", schema = "auth")
public class RoleAuthorities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_authority_id")
    private long roleAuthorityId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authority_id")
    private Authority authority;
}
