package us.group14.backend.registration.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.group14.backend.user.User;
import us.group14.backend.user.UserRole;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.within;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    private ConfirmationTokenService confirmationTokenService;
    @BeforeEach
    void setUp() {
        confirmationTokenService = new ConfirmationTokenService(confirmationTokenRepository);

    }

    @Test
    void createConfirmationToken() {
        User user1 = new User("Bob", "bob1@gmail.com", "bobby123", UserRole.USER);
        confirmationTokenService.createConfirmationToken(user1);

        ArgumentCaptor<ConfirmationToken> tokenCaptor = ArgumentCaptor.forClass(ConfirmationToken.class);
        verify(confirmationTokenRepository).save(tokenCaptor.capture());

        assertThat(tokenCaptor.getValue().getUser()).isEqualTo(user1);
//        assertThat(tokenCaptor.getValue().getToken())
        assertThatNoException().isThrownBy(() -> {
            //if token is not a valid UUID it will throw an illegal argument exception
            UUID.fromString(tokenCaptor.getValue().getToken());
        });

        assertThat(tokenCaptor.getValue().getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(tokenCaptor.getValue().getExpiredAt()).isCloseTo(LocalDateTime.now().plusMinutes(15), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void getToken() {
        String token = "test1";
        confirmationTokenService.getToken(token);

        // tool that can be used to capture inputs
        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        // capturing the input
        verify(confirmationTokenRepository).findByToken(tokenCaptor.capture());

        assertThat(tokenCaptor.getValue()).isEqualTo(token);


    }

    @Test
    void setConfirmedAt() {
        String token = "test2";
        confirmationTokenService.setConfirmedAt(token);

        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<LocalDateTime> dateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        //capturing the input
        verify(confirmationTokenRepository).updateConfirmedAt(tokenCaptor.capture(), dateCaptor.capture() );

        assertThat(tokenCaptor.getValue()).isEqualTo(token);
        //is the time close to the current time with a maximum difference of a second
        assertThat(dateCaptor.getValue()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }
}