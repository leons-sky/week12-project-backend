package us.group14.backend.transaction;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import us.group14.backend.constants.ApiResponse;
import us.group14.backend.user.User;

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
        Pageable recent = PageRequest.of(0, limit, Sort.Direction.DESC, "id");
        return ResponseEntity.ok(transactionRepository.findFromAccountWithPageable(account, recent));
    }

    public ResponseEntity<Transaction> getTransaction(User user, Long id) {
        Account account = user.getAccount();
        return ResponseEntity.ok(transactionRepository.findFromAccountWithId(account, id).orElse(null));
    }
}
