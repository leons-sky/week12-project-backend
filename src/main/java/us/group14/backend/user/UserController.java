package us.group14.backend.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.group14.backend.annotations.ApiMapping;

import java.util.List;
import java.util.Set;

@RestController
@ApiMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    @JsonView(UserView.class)
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/contacts/{id}")
    public void addContact(AuthUser user, @PathVariable("id") Long id) {
        userService.addContact(user.get(), id);
    }

    @GetMapping("/contacts")
    public Set<User> getContacts(AuthUser user){
        return userService.getContacts(user.get());
    }

    @DeleteMapping("/contacts/{id}")
    public String deleteContact(AuthUser user, @PathVariable("id") Long id) {
        userService.deleteContact(user.get(), id);
//        AuthUser deleteContact = authUserRepository.findById(id).get();
//        authUserRepository.delete(deleteContact);
//        return "Contact deleted";
    }



}
