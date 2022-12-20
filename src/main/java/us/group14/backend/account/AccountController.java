package us.group14.backend.account;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.group14.backend.annotations.ApiMapping;
import us.group14.backend.transaction.Transaction;
import us.group14.backend.views.AccountView;
import us.group14.backend.views.TransactionAndAccountView;
import us.group14.backend.transaction.TransactionRequest;
import us.group14.backend.transaction.TransactionService;
import us.group14.backend.user.AuthUser;
import us.group14.backend.user.User;

@RestController
@ApiMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping
    @JsonView(AccountView.class)
    public ResponseEntity<Account> getAccount(AuthUser authUser) {
        User user = authUser.get();
        return accountService.getAccount(user);
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(AuthUser authUser) {
        User user = authUser.get();
        return accountService.getBalance(user);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(AuthUser authUser, @Valid @RequestBody TransactionRequest request) {
        User user = authUser.get();
        return accountService.withdraw(user, request);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(AuthUser authUser, @Valid @RequestBody TransactionRequest request) {
        User user = authUser.get();
        return accountService.deposit(user, request);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(AuthUser authUser, @Valid @RequestBody TransactionRequest request) {
        User user = authUser.get();
        return accountService.transfer(user, request);
    }

    @GetMapping("/transactions")
    @JsonView(TransactionAndAccountView.class)
    public ResponseEntity<?> getTransactions(
            AuthUser authUser,
            @Valid
            @RequestParam(value = "limit", defaultValue = "100")
            @Range(min = 0, max = 250)
            Integer limit
    ) {
        User user = authUser.get();
        return transactionService.getTransactions(user, limit);
    }

    @GetMapping("/transactions/{id}")
    @JsonView(TransactionAndAccountView.class)
    public ResponseEntity<Transaction> getTransaction(AuthUser authUser, @Valid @PathVariable("id") Long id) {
        User user = authUser.get();
        return transactionService.getTransaction(user, id);
    }
}
