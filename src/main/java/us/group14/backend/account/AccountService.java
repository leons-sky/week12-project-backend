package us.group14.backend.account;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import us.group14.backend.constants.ApiResponse;
import us.group14.backend.transaction.Transaction;
import us.group14.backend.transaction.TransactionRepository;
import us.group14.backend.transaction.TransactionRequest;
import us.group14.backend.transaction.TransactionType;
import us.group14.backend.user.User;
import us.group14.backend.user.UserDetailsService;

@Service
@AllArgsConstructor
public class AccountService {

    private final UserDetailsService userDetailsService;
    private final TransactionRepository transactionRepository;

    public ResponseEntity<Account> getAccount(User user) {
        return ResponseEntity.ok(user.getAccount());
    }

    public ResponseEntity<Double> getBalance(User user) {
        Account account = user.getAccount();
        return ResponseEntity.ok(account.getBalance());
    }

    @Transactional
    public ResponseEntity<String> withdraw(User user, TransactionRequest request) {
        Account account = user.getAccount();
        Double amount = request.getAmount();
        if (amount == null) {
            return ApiResponse.INVALID_AMOUNT.toResponse();
        }
        if (amount > account.getBalance()) {
            return ApiResponse.EXCEEDS_BALANCE.toResponse();
        }

        Transaction transaction = new Transaction(amount, account, TransactionType.WITHDRAW);
        account.complete(transaction);

        return ApiResponse.OK.toResponse();
    }

    @Transactional
    public ResponseEntity<String> deposit(User user, TransactionRequest request) {
        Account account = user.getAccount();
        Double amount = request.getAmount();
        if (amount == null) {
            return ApiResponse.INVALID_AMOUNT.toResponse();
        }

        Transaction transaction = new Transaction(amount, account, TransactionType.DEPOSIT);
        account.complete(transaction);

        return ApiResponse.OK.toResponse();
    }

    @Transactional
    public ResponseEntity<String> transfer(User user, TransactionRequest request) {
        User recipient = userDetailsService.getUserById(request.getRecipientId());
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
        Transaction transaction = new Transaction(amount, senderAccount, recipientAccount, TransactionType.TRANSFER);
        senderAccount.complete(transaction);
        recipientAccount.complete(transaction);

        return ApiResponse.OK.toResponse();
    }

}
