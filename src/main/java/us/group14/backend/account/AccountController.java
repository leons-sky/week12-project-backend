package us.group14.backend.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.group14.backend.annotations.ApiMapping;
import us.group14.backend.user.User;

@RestController
@ApiMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    @GetMapping
    public ResponseEntity<Account> getAccount(Authentication auth) {
        User user = (User) auth.getPrincipal();
    }

}
