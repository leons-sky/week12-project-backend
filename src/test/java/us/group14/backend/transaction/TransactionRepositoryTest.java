package us.group14.backend.transaction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import us.group14.backend.account.Account;
import us.group14.backend.account.AccountRepository;
import us.group14.backend.user.User;
import us.group14.backend.user.UserRepository;
import us.group14.backend.user.UserRole;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }

    @Test
    void findFromAccount() {
        User user = userRepo.save(new User("username", "email", "password", UserRole.USER));
        Account account = accountRepository.save(new Account());
        Transaction transaction = transactionRepository.save(new Transaction(5.5, TransactionType.DEPOSIT));

        user.setAccount(account);
        account.setUser(user);

        userRepo.save(user);
        accountRepository.save(account);

        transaction.complete(account);

        Set<Transaction> Transactions = transactionRepository.findFromAccount(account.getId());

        assertThat(Transactions).isNotNull();
        // testing if null & if it contains transaction that was added
        assertThat(Transactions.stream().toList()).asList().contains(transaction);
    }

    @Test
    void findFromAccountWithId() {
        User user = userRepo.save(new User("username", "email", "password", UserRole.USER));
        Account account = accountRepository.save(new Account());
        Transaction transaction = transactionRepository.save(new Transaction(5.5, TransactionType.DEPOSIT));

        user.setAccount(account);
        account.setUser(user);

        userRepo.save(user);
        accountRepository.save(account);

        transaction.complete(account);

        Optional<Transaction> transaction1 = transactionRepository.findFromAccountWithId(account.getId(), transaction.getId());

        assertThat(transaction1.get().equals(transaction)).isTrue();

    }
}