package us.group14.backend.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepo;

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }

    @Test
    void findByUsername() {
        User user = userRepo.save(new User("username", "email", "password", UserRole.USER));
        Optional <User> foundUser = userRepo.findByUsername("username");

        assertThat(foundUser.get().equals(user)).isTrue();

    }

    @Test
    void enableUser() {
        User user = userRepo.save(new User("username", "email", "password", UserRole.USER));
        Integer success = userRepo.enableUser("username");

        assertThat(success).isEqualTo(1);




    }
}