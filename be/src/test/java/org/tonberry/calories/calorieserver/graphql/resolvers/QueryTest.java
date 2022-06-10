package org.tonberry.calories.calorieserver.graphql.resolvers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.tonberry.calories.calorieserver.controllers.MockitoUnitTest;

class QueryTest extends MockitoUnitTest {


    @InjectMocks
    Query query;

    @Test
    void version() {
        query.version(1L,null );
    }
}