package us.group14.backend.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import us.group14.backend.annotations.ApiMapping;
import us.group14.backend.user.UserService;

import static us.group14.backend.security.config.AuthFilter.getAuthCookie;

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

    @PostMapping("login")
    public ResponseEntity<String> authenticate(HttpServletResponse response, @RequestBody AuthenticationRequest request) {
        return userService.authenticate(response, request);
    }

    @PostMapping("logout")
    public ResponseEntity<String> unauthenticate(HttpServletRequest request, HttpServletResponse response) {
        return userService.unauthenticate(request, response);
    }

}
