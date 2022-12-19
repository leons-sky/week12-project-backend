package us.group14.backend.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import us.group14.backend.account.Account;
import us.group14.backend.account.AccountRepository;
import us.group14.backend.constants.ApiResponse;
import us.group14.backend.registration.token.ConfirmationToken;
import us.group14.backend.registration.token.ConfirmationTokenService;
import us.group14.backend.security.AuthenticationRequest;
import us.group14.backend.security.config.JwtUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final Argon2PasswordEncoder argon2PasswordEncoder;
    private final UserDetailsService userDetailsService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ContactRepository contactRepository;

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<String> register(User user) {
        boolean userExists = userRepository.findByUsername(user.getUsername()).isPresent();

        if (userExists) {
            return ApiResponse.USER_TAKEN.toResponse();
        }

        String encodedPassword = argon2PasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Account account = accountRepository.save(new Account());
        User savedUser = userRepository.save(user);

        savedUser.setAccount(account);
        account.setUser(savedUser);

        userRepository.save(savedUser);
        accountRepository.save(account);

        ConfirmationToken token = confirmationTokenService.createConfirmationToken(user);
        return ResponseEntity.ok(token.getToken());
    }

    public int enable(User user) {
        return userRepository.enableUser(user.getUsername());
    }

    public ResponseEntity<String> authenticate(HttpServletResponse response, AuthenticationRequest request) {
        return this.authenticate(response, request.getUsername(), request.getPassword());
    }

    public ResponseEntity<String> authenticate(HttpServletResponse response, String username, String password) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return ApiResponse.USER_AUTH_FAILED.toResponse();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                            password
                    )
            );
        } catch (DisabledException e) {
            return ApiResponse.USER_DISABLED.toResponse();
        } catch (LockedException e) {
            return ApiResponse.USER_LOCKED.toResponse();
        } catch (BadCredentialsException e){
            return ApiResponse.USER_AUTH_FAILED.toResponse();
        }

        userDetailsService.authenticate(response, jwtUtil.generateToken(userDetails));

        return ApiResponse.OK.toResponse();
    }

    public ResponseEntity<String> unauthenticate(HttpServletRequest request, HttpServletResponse response) {
        userDetailsService.unauthenticate(request, response);

        return ApiResponse.OK.toResponse();
    }

    public ResponseEntity<String> addContact(User user, String username) {
        User contactUser;
        try {
            contactUser = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return ApiResponse.USER_NOT_FOUND.toResponse();
        }

        if (contactRepository.getContactFromUser(user.getId(), contactUser.getId()).isPresent()) {
            return ApiResponse.CONTACT_ALREADY_ADDED.toResponse();
        }

        contactRepository.save(new Contact(user, contactUser));
        userRepository.save(user);

        return ApiResponse.OK.toResponse();
    }

    public ResponseEntity<Set<Contact>> getContacts(User user) {
        return ResponseEntity.ok(contactRepository.getContactsForUser(user.getId()));
    }

    public ResponseEntity<String> deleteContact(User user, String username) {
        User contactUser;
        try {
            contactUser = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return ApiResponse.USER_NOT_FOUND.toResponse();
        }

        Optional<Contact> contactOptional = contactRepository.getContactFromUser(user.getId(), contactUser.getId());
        if (contactOptional.isEmpty()) {
            return ApiResponse.CONTACT_NOT_FOUND.toResponse();
        }

        Contact contact = contactOptional.get();
        contactRepository.delete(contact);
        userRepository.save(user);

        return ApiResponse.OK.toResponse();
    }
}
