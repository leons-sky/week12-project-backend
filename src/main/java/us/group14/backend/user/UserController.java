package us.group14.backend.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.group14.backend.annotations.ApiMapping;

import java.util.List;

@RestController
@ApiMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @JsonView(UserView.class)
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

}
