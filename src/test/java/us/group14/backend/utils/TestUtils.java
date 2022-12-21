package us.group14.backend.utils;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import us.group14.backend.account.Account;
import us.group14.backend.account.AccountRepository;
import us.group14.backend.constants.ApiCookie;
import us.group14.backend.registration.token.ConfirmationToken;
import us.group14.backend.registration.token.ConfirmationTokenRepository;
import us.group14.backend.security.config.JwtUtil;
import us.group14.backend.transaction.TransactionRepository;
import us.group14.backend.user.ContactRepository;
import us.group14.backend.user.User;
import us.group14.backend.user.UserRepository;
import us.group14.backend.user.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@ContextConfiguration(classes = {UserRepository.class, ContactRepository.class, AccountRepository.class,
        TransactionRepository.class, ConfirmationTokenRepository.class, JwtUtil.class})
@AllArgsConstructor
public class TestUtils {
    private UserRepository userRepository;
    private ContactRepository contactRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private JwtUtil jwtUtil;

    public String getLink(int port, String path) {
        return String.format("http://localhost:%d/api/v1/%s", port, path);
    }

    public void clearDatabase() {
        contactRepository.deleteAll();
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        confirmationTokenRepository.deleteAll();
    }

    public ResponseEntity<Object> request(TestRestTemplate restTemplate, String url, HttpMethod method, HttpEntity entity) {
        return restTemplate.exchange(url, method, entity, Object.class);
    }

    public HttpEntity<String> getHttpEntity(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", String.format("%s=%s", ApiCookie.AUTH_COOKIE.get(), jwtUtil.generateToken(user)));

        return new HttpEntity<>(headers);
    }

    public User createTestUser() {
        User user = userRepository.save(new User("test", "test@email.com", "password", UserRole.USER));
        String uuid = UUID.randomUUID().toString();
        ConfirmationToken token = confirmationTokenRepository.save(new ConfirmationToken(uuid, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user));
        token.setConfirmedAt(LocalDateTime.now());
        user.setEnabled(true);

        Account account = accountRepository.save(new Account());
        user.setAccount(account);
        account.setUser(user);

        userRepository.save(user);
        accountRepository.save(account);
        confirmationTokenRepository.save(token);

        return user;
    }
}
