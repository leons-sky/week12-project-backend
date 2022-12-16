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

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public ResponseEntity<?> getTransactions(User user, Integer limit) {
        if (limit == null) {
            limit = 100;
        } else if (limit < 0 || limit > 100) {
            return ApiResponse.INVALID_LIMIT.toResponse();
        }

        Account account = user.getAccount();
        List<Transaction> transactions = account.getTransactions().stream().sorted(Comparator.comparing(Transaction::getTransferredAt).reversed()).limit(limit).toList();
//        Pageable recent = PageRequest.of(0, limit, Sort.Direction.DESC, "id");
//        Page<Transaction> transactions = transactionRepository.findFromAccountWithPageable(account.getId(), recent);
        return ResponseEntity.ok(transactions);
    }

    public ResponseEntity<Transaction> getTransaction(User user, Long id) {
        Account account = user.getAccount();
        Transaction transaction = account.getTransactions().stream().filter(t -> t.getId() == id).findFirst().orElse(null);
//        Transaction transaction = transactionRepository.findFromAccountWithId(account.getId(), id).orElse(null);
        return ResponseEntity.ok(transaction);
    }
}
