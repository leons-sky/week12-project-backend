package us.group14.backend.security.config;

import com.leonsemmens.securitycourse.constants.ApiCookie;
import com.leonsemmens.securitycourse.user.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public static Cookie getAuthCookie(HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        final Optional<Cookie> authCookieOptional = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(ApiCookie.AUTH_COOKIE.get())).findFirst();
        return authCookieOptional.orElse(null);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final Cookie authCookie = getAuthCookie(request);
        if (authCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authCookie.getValue();
        final String username = jwtUtil.extractUsername(jwtToken);

        SecurityContext context = SecurityContextHolder.getContext();
        if (username != null && context.getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (userDetails == null) {
                // TODO: respond with USER_NOT_FOUND error
            }

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities())
                        ;
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
