package us.group14.backend.security;

import com.leonsemmens.securitycourse.annotations.ApiMapping;
import com.leonsemmens.securitycourse.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.leonsemmens.securitycourse.security.config.AuthFilter.getAuthCookie;

@RestController
@ApiMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Boolean> check(HttpServletRequest request) {
        Cookie authCookie = getAuthCookie(request);
        return ResponseEntity.ok(authCookie != null);
    }

    @PostMapping
    public ResponseEntity<String> authenticate(HttpServletResponse response, @RequestBody AuthenticationRequest request) {
        return userService.authenticate(response, request);
    }

}
