package us.group14.backend.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.recipient.id = ?1 OR t.sender.id = ?1")
    Set<Transaction> findFromAccount(Long accountId);

    @Query("SELECT t FROM Transaction t WHERE (t.recipient.id = ?1 OR t.sender.id = ?1) AND t.id = ?2")
    Optional<Transaction> findFromAccountWithId(Long accountId, Long transactionId);
}
