package us.group14.backend.registration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.group14.backend.annotations.ApiMapping;

@RestController
@ApiMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping("confirm")
    public ResponseEntity<String> confirm(@Valid @RequestParam("token") @NotBlank String token) {
        return registrationService.confirmToken(token);
    }
}
