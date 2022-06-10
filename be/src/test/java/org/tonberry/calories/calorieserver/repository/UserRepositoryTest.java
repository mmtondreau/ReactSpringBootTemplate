package org.tonberry.calories.calorieserver.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.tonberry.calories.calorieserver.persistence.auth.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest extends RepositoryTest{

    @Autowired
    UserRepository userRepository;

    @Test
    void findByUsername() {
        User user = User.builder().withUsername("test").build();

        userRepository.save(user);
        Optional<User> result = userRepository.findByUsername("test");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("test");

    }
}