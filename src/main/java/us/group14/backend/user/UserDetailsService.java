package us.group14.backend.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import us.group14.backend.constants.ApiCookie;
import us.group14.backend.constants.ApiResponse;

@Service
@AllArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("User with username '%s' not found", username)
                )
        );
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    private Cookie createAuthCookie(String token) {
        Cookie cookie = new Cookie(ApiCookie.AUTH_COOKIE.get(), token);
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(token == null ? 0 : 60 * 60 * 24);
        return cookie;
    }

    public void authenticate(HttpServletResponse response, String token) {
        Cookie cookie = createAuthCookie(token);
        response.addCookie(cookie);
    }

    public void unauthenticate(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = createAuthCookie(null);
        response.addCookie(cookie);

        HttpSession session = request.getSession(false);
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.invalidate();
        }
    }
}
