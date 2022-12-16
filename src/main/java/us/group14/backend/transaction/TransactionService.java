package us.group14.backend.transaction;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import us.group14.backend.account.Account;
import us.group14.backend.constants.ApiResponse;
import us.group14.backend.user.User;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public ResponseEntity<?> getTransactions(User user, Integer limit) {
        if (limit < 0 || limit > 100) {
            return ApiResponse.INVALID_LIMIT.toResponse();
        }

        Account account = user.getAccount();
        List<Transaction> transactions = transactionRepository.findFromAccount(account.getId())
                .stream().sorted(Comparator.comparing(Transaction::getTransferredAt).reversed()).limit(limit).toList();

        return ResponseEntity.ok(transactions);
    }

    public ResponseEntity<Transaction> getTransaction(User user, Long id) {
        Account account = user.getAccount();
        Transaction transaction = transactionRepository.findFromAccountWithId(account.getId(), id).orElse(null);
        return ResponseEntity.ok(transaction);
    }
}
