package us.group14.backend.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.group14.backend.annotations.ApiMapping;
import us.group14.backend.constants.ApiResponse;
import us.group14.backend.transaction.Transaction;
import us.group14.backend.transaction.TransactionRequest;
import us.group14.backend.transaction.TransactionService;
import us.group14.backend.user.AuthUser;
import us.group14.backend.user.User;

import java.util.List;

@RestController
@ApiMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping
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
    public ResponseEntity<String> withdraw(AuthUser authUser, @RequestBody TransactionRequest request) {
        User user = authUser.get();
        return accountService.withdraw(user, request);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(AuthUser authUser, @RequestBody TransactionRequest request) {
        User user = authUser.get();
        return accountService.deposit(user, request);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(AuthUser authUser, @RequestBody TransactionRequest request) {
        User user = authUser.get();
        return accountService.transfer(user, request);
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(AuthUser authUser, @RequestParam("limit") Integer limit) {
        User user = authUser.get();
        return transactionService.getTransactions(user, limit);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(AuthUser authUser, @PathVariable("id") Long id) {
        User user = authUser.get();
        return transactionService.getTransaction(user, id);
    }
}
