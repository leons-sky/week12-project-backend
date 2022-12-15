package us.group14.backend.user;

import com.leonsemmens.securitycourse.constants.ApiCookie;
import com.leonsemmens.securitycourse.constants.ApiResponse;
import com.leonsemmens.securitycourse.registration.token.ConfirmationToken;
import com.leonsemmens.securitycourse.registration.token.ConfirmationTokenService;
import com.leonsemmens.securitycourse.security.AuthenticationRequest;
import com.leonsemmens.securitycourse.security.config.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
