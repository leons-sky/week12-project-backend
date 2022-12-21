package us.group14.backend.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    @NotBlank(message = "Username is required")
    private final String username;
    @NotBlank(message = "Email is required")
    private final String email;
    @NotBlank(message = "Password is required")
    private final String password;

}
