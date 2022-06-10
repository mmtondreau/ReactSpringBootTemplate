package org.tonberry.calories.calorieserver.graphql;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.tonberry.calories.calorieserver.graphql.resolvers.Query;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class GraphQLProvider {
    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        try {
            GraphQLSchema graphQLSchema = SchemaParser.newParser().file("graphql/schema.graphqls")
                    .resolvers(applicationContext.getBean(Query.class))
                    .build().makeExecutableSchema();
            this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

}
