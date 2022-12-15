package us.group14.backend.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
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

        userRepository.save(user);

        ConfirmationToken token = confirmationTokenService.createConfirmationToken(user);
        return ResponseEntity.ok(token.getToken());
    }

    public int enable(User user) {
        return userRepository.enableUser(user.getUsername());
    }

    public ResponseEntity<String> authenticate(HttpServletResponse response, AuthenticationRequest request) {
        return this.authenticate(response, request.getUsername(), request.getPassword());
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

        Cookie cookie = new Cookie(ApiCookie.AUTH_COOKIE.get(), jwtUtil.generateToken(userDetails));
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);

        response.addCookie(cookie);

        return ApiResponse.OK.toResponse();
    }

    @Transactional
    public void addContact(User user, Long id) {
        User contact = userRepository.findById(id).orElse(null);
        user.addContact(contact);
    }

    public Set<User> getContacts(User user) {
        return user.getContacts();
    }

    @Transactional
    public void deleteContact(User user, Long id) {
        User contact = userRepository.findById(id).orElse(null);
        user.deleteContact(contact);


    }
}
