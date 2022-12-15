package us.group14.backend.registration;

import com.leonsemmens.securitycourse.annotations.ApiMapping;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @PostMapping("confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
