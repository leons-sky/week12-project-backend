package us.group14.backend.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository
        extends JpaRepository <Transaction, Long> {
}
