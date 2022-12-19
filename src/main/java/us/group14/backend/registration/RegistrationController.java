package us.group14.backend.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.group14.backend.annotations.ApiMapping;

@RestController
@ApiMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping("confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
