package us.group14.backend.registration;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import us.group14.backend.constants.ApiResponse;
import us.group14.backend.email.EmailService;
import us.group14.backend.registration.token.ConfirmationEmail;
import us.group14.backend.registration.token.ConfirmationToken;
import us.group14.backend.registration.token.ConfirmationTokenService;
import us.group14.backend.user.User;
import us.group14.backend.user.UserRole;
import us.group14.backend.user.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailService emailService;
    private final EmailValidator emailValidator;
    private final ConfirmationEmail confirmationEmail;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    public ResponseEntity<String> register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            return ApiResponse.INVALID_EMAIL.toResponse();
        }

        ResponseEntity<String> response = userService.register(new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                UserRole.USER
        ));

        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        String token = response.getBody();
        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailService.send(request.getEmail(), confirmationEmail.build(request.getUsername(), link));

        return response;
    }

    @Transactional
    public ResponseEntity<String> confirmToken(String token) {
        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenService.getToken(token);
        if (confirmationTokenOptional.isEmpty()) {
            return ApiResponse.CONFIRMATION_TOKEN_NOT_FOUND.toResponse();
        }

        ConfirmationToken confirmationToken = confirmationTokenOptional.get();
        if (confirmationToken.getConfirmedAt() != null) {
            return ApiResponse.ALREADY_CONFIRMED_EMAIL.toResponse();
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return ApiResponse.CONFIRMATION_TOKEN_EXPIRED.toResponse();
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enable(confirmationToken.getUser());
        return ApiResponse.OK.toResponse();
    }
}
