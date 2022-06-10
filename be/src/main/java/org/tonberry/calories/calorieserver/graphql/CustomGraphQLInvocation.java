package org.tonberry.calories.calorieserver.graphql;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.spring.web.servlet.GraphQLInvocation;
import graphql.spring.web.servlet.GraphQLInvocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.CompletableFuture;

@Component
@Primary
public class CustomGraphQLInvocation implements GraphQLInvocation {
    @Autowired
    GraphQL graphQL;

    @Override
    public CompletableFuture<ExecutionResult> invoke(GraphQLInvocationData graphQLInvocationData, WebRequest webRequest) {
        return graphQL.executeAsync(ExecutionInput.newExecutionInput()
                .query(graphQLInvocationData.getQuery())
                .context(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .operationName(graphQLInvocationData.getOperationName())
                .variables(graphQLInvocationData.getVariables())
                .build());

    }
}
