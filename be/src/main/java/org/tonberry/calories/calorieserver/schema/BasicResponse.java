package org.tonberry.calories.calorieserver.schema;

import lombok.*;

@Setter
@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponse {
    private String status;
    private String message;
}
