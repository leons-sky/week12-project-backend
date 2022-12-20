package us.group14.backend.account;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import us.group14.backend.constants.ApiResponse;
import us.group14.backend.transaction.Transaction;
import us.group14.backend.transaction.TransactionRepository;
import us.group14.backend.transaction.TransactionRequest;
import us.group14.backend.transaction.TransactionType;
import us.group14.backend.user.User;
import us.group14.backend.user.UserDetailsService;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private final UserDetailsService userDetailsService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public ResponseEntity<Account> getAccount(User user) {
        return ResponseEntity.ok(user.getAccount());
    }

    public ResponseEntity<Double> getBalance(User user) {
        Account account = user.getAccount();
        return ResponseEntity.ok(account.getBalance());
    }

//    @Transactional
    public ResponseEntity<String> withdraw(User user, TransactionRequest request) {
        Account account = user.getAccount();
        Double amount = request.getAmount();
        if (amount == null) {
            return ApiResponse.INVALID_AMOUNT.toResponse();
        }
        if (amount > account.getBalance()) {
            return ApiResponse.EXCEEDS_BALANCE.toResponse();
        }

        Transaction transaction = transactionRepository.save(
                new Transaction(amount, TransactionType.WITHDRAW)
        );

        transaction.complete(account);

        transactionRepository.save(transaction);
        accountRepository.save(account);

        return ApiResponse.OK.toResponse();
    }

//    @Transactional
    public ResponseEntity<String> deposit(User user, TransactionRequest request) {
        Account account = user.getAccount();
        Double amount = request.getAmount();
        if (amount == null) {
            return ApiResponse.INVALID_AMOUNT.toResponse();
        }

        System.out.println(amount);

        Transaction transaction = transactionRepository.save(
                new Transaction(amount, TransactionType.DEPOSIT)
        );

        transaction.complete(account);

        transactionRepository.save(transaction);
        accountRepository.save(account);

        return ApiResponse.OK.toResponse();
    }

//    @Transactional
    public ResponseEntity<String> transfer(User user, TransactionRequest request) {
        User recipient = userDetailsService.loadUserByUsername(request.getRecipient());
        if (recipient == null) {
            return ApiResponse.USER_NOT_FOUND.toResponse();
        }

        Account senderAccount = user.getAccount();
        Double amount = request.getAmount();
        if (amount == null) {
            return ApiResponse.INVALID_AMOUNT.toResponse();
        }
        if (amount > senderAccount.getBalance()) {
            return ApiResponse.EXCEEDS_BALANCE.toResponse();
        }

        Account recipientAccount = recipient.getAccount();
        Transaction transaction = transactionRepository.save(
                new Transaction(amount, TransactionType.TRANSFER, request.getMessage())
        );

        transaction.complete(recipientAccount, senderAccount);

        transactionRepository.save(transaction);
        accountRepository.saveAll(List.of(senderAccount, recipientAccount));

        return ApiResponse.OK.toResponse();
    }

}
