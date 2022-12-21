package us.group14.backend.security;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Username is required")
    private final String username;
    @NotBlank(message = "Password is required")
    private final String password;

}
