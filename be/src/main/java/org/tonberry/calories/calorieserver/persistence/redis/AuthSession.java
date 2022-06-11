package org.tonberry.calories.calorieserver.persistence.redis;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("auth-session")
public class AuthSession implements Serializable {
    @Id
    private String id;
    private long sessionId;
    private long userId;
    private Date expiration;
}
