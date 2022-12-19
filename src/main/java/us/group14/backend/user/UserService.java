package us.group14.backend.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import us.group14.backend.account.Account;
import us.group14.backend.account.AccountRepository;
import us.group14.backend.constants.ApiCookie;
import us.group14.backend.constants.ApiResponse;
import us.group14.backend.registration.token.ConfirmationToken;
import us.group14.backend.registration.token.ConfirmationTokenService;
import us.group14.backend.security.AuthenticationRequest;
import us.group14.backend.security.config.JwtUtil;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final Argon2PasswordEncoder argon2PasswordEncoder;
    private final UserDetailsService userDetailsService;
    private final ConfirmationTokenService confirmationTokenService;

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<String> register(User user) {
        boolean userExists = userRepository.findByUsername(user.getUsername()).isPresent();

        if (userExists) {
            return ApiResponse.USER_TAKEN.toResponse();
        }

        String encodedPassword = argon2PasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Account account = accountRepository.save(new Account());
        User savedUser = userRepository.save(user);

        savedUser.setAccount(account);
        account.setUser(savedUser);

        userRepository.save(savedUser);
        accountRepository.save(account);

        ConfirmationToken token = confirmationTokenService.createConfirmationToken(user);
        return ResponseEntity.ok(token.getToken());
    }

    public int enable(User user) {
        return userRepository.enableUser(user.getUsername());
    }

    public ResponseEntity<String> authenticate(HttpServletResponse response, AuthenticationRequest request) {
        return this.authenticate(response, request.getUsername(), request.getPassword());
    }

    private Cookie createAuthCookie(String token) {
        Cookie cookie = new Cookie(ApiCookie.AUTH_COOKIE.get(), token);
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(token == null ? 0 : 60 * 60 * 24);
        return cookie;
    }

    public ResponseEntity<String> authenticate(HttpServletResponse response, String username, String password) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null){
            return ApiResponse.USER_AUTH_FAILED.toResponse();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                            password
                    )
            );
        } catch (DisabledException e) {
            return ApiResponse.USER_DISABLED.toResponse();
        } catch (LockedException e) {
            return ApiResponse.USER_LOCKED.toResponse();
        } catch (BadCredentialsException e){
            return ApiResponse.USER_AUTH_FAILED.toResponse();
        }

        Cookie cookie = createAuthCookie(jwtUtil.generateToken(userDetails));
        response.addCookie(cookie);

        return ApiResponse.OK.toResponse();
    }

    public ResponseEntity<String> unauthenticate(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = createAuthCookie(null);
        response.addCookie(cookie);

        HttpSession session = request.getSession(false);
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.invalidate();
        }

        return ApiResponse.OK.toResponse();
    }

    @Transactional
    public ResponseEntity<String> addContact(User user, String username) {
        User contact;
        try {
            contact = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return ApiResponse.USER_NOT_FOUND.toResponse();
        }

        user.addContact(contact);
        return ApiResponse.OK.toResponse();
    }

    @Transactional
    public ResponseEntity<Set<User>> getContacts(User user) {
        return ResponseEntity.ok(user.getContacts());
    }

    @Transactional
    public ResponseEntity<String> deleteContact(User user, Long id) {
        User contact = userRepository.findById(id).orElse(null);
        if (contact == null) {
            return ApiResponse.USER_NOT_FOUND.toResponse();
        }

        user.deleteContact(contact);
        return ApiResponse.OK.toResponse();
    }
}
