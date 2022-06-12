package org.tonberry.calories.calorieserver.schema.user;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleRequest {
    private String name;
    private List<String> authorities;
}
