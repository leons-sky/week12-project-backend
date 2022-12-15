package us.group14.backend.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.group14.backend.user.User;

@Repository
public interface AccountRepository
        extends JpaRepository <Account, Long> {
}

