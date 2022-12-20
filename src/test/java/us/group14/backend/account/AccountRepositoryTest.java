package us.group14.backend.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import us.group14.backend.user.User;
import us.group14.backend.user.UserRepository;
import us.group14.backend.user.UserRole;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAccountForUser() {
        User user = userRepository.save(new User("fred", "fred@email.com", "password", UserRole.USER));
        Account account = accountRepository.save(new Account());

        user.setAccount(account);
        account.setUser(user);

        userRepository.save(user);
        accountRepository.save(account);

        Account foundAccount = accountRepository.getAccountForUser(user.getId());
        assertThat(account.equals(foundAccount)).isTrue();
    }
}