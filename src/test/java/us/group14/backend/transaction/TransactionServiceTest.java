package us.group14.backend.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.group14.backend.account.Account;
import us.group14.backend.registration.token.ConfirmationToken;
import us.group14.backend.registration.token.ConfirmationTokenRepository;
import us.group14.backend.registration.token.ConfirmationTokenService;
import us.group14.backend.user.User;
import us.group14.backend.user.UserRole;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;


    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository);
    }


//    @Test
//    void getTransactions() {
//        User user5 = new User("Pat", "pat@gmail.com", "pat123", UserRole.USER);
//        Account account = new Account();
//        user5.setAccount(account);
//        account.setUser(user5);
//
//        //needs second argument (integer limit)
//        transactionService.getTransactions(user5, 100);
//
//        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
//        verify(transactionRepository).save(transactionCaptor.capture());
//
//        assertThat(transactionCaptor.getValue()).isEqualTo(user5);
//    }

//    @Test
//    void getTransaction() {
//        User user2 = new User("Rob", "rob@gmail.com", "rob123", UserRole.USER);
//
//        //needs second argument (long ID)
//        transactionService.getTransaction(user2, );
//
//        ArgumentCaptor<ConfirmationToken> tokenCaptor = ArgumentCaptor.forClass(ConfirmationToken.class);
//        verify(transactionService).save(tokenCaptor.capture());
//
//        assertThat(tokenCaptor.getValue().getUser()).isEqualTo(user2);
//
//    }
}