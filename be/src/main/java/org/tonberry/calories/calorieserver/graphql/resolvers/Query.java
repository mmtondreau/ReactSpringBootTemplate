package org.tonberry.calories.calorieserver.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tonberry.calories.calorieserver.graphql.types.VersionType;

@Component
@Slf4j
public class Query implements GraphQLQueryResolver {

    public VersionType version(Long id, DataFetchingEnvironment environment) {
        return VersionType.builder()
                .withId(1L)
                .withName("Unknown")
                .build();
    }
}
