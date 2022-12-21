package us.group14.backend.registration.token;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import us.group14.backend.user.User;
import us.group14.backend.user.UserRepository;
import us.group14.backend.user.UserRole;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ConfirmationTokenRepositoryTest {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserRepository userRepository;
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        confirmationTokenRepository.deleteAll();
    }

    @Test
    void findByToken() {
        User user = userRepository.save(new User("username", "email", "password", UserRole.USER));
        ConfirmationToken Token = confirmationTokenRepository.save(new ConfirmationToken("string", LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), user));

        Optional<ConfirmationToken> FoundToken = confirmationTokenRepository.findByToken("string");

        assertThat (Token.equals(FoundToken.get())).isTrue();
    }

    @Test
    void updateConfirmedAt() {
        User user = userRepository.save(new User("username", "email", "password", UserRole.USER));
        ConfirmationToken Token = confirmationTokenRepository.save(new ConfirmationToken("string", LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), user));
        LocalDateTime DateTime = LocalDateTime.now();
        int success = confirmationTokenRepository.updateConfirmedAt("string", DateTime);
        assertThat(success).isEqualTo(1);

    }
}