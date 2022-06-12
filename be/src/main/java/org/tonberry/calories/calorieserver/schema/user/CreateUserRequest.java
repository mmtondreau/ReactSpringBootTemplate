package org.tonberry.calories.calorieserver.schema.user;

import lombok.*;

@Setter
@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String username;
    private String password;
    private String apiKey;
    private String role;
}
