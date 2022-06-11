package org.tonberry.calories.calorieserver.schema;

import lombok.*;

@Setter
@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatRequest {
    private String username;
    private String password;
    private String scope;
    private Long exp;
}
