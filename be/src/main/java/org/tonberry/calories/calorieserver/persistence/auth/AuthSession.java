package org.tonberry.calories.calorieserver.persistence.auth;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "session", schema = "auth")
public class AuthSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private long sessionId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "token")
    private String token;

    @Column(name = "expiration")
    private Date expiration;
}
