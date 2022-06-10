package org.tonberry.calories.calorieserver.persistence.auth;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authority", schema = "auth")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private long authorityId;

    @Column(name = "name")
    private String name;
}
