package org.tonberry.calories.calorieserver.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class RepositoryTest {
}
