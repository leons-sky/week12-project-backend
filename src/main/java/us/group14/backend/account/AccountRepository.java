package us.group14.backend.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository
        extends JpaRepository <Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.user.id = ?1")
    Account getAccountForUser(Long userId);
}

