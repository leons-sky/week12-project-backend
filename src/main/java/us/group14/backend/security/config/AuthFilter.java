package us.group14.backend.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import us.group14.backend.constants.ApiCookie;
import us.group14.backend.constants.ApiResponse;
import us.group14.backend.user.User;
import us.group14.backend.user.UserDetailsService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

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
            User user;
            try {
                user = userDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                System.out.println("ERROR");
                ResponseEntity<String> responseEntity = ApiResponse.USER_NOT_FOUND.toResponse();

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(objectMapper.writeValueAsString(responseEntity));
                return;
            } catch (Exception e) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(e.getMessage());
                return;
            }

            if (jwtUtil.validateToken(jwtToken, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities())
                        ;
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
