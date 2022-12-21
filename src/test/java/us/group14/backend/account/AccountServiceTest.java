package us.group14.backend.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import us.group14.backend.transaction.Transaction;
import us.group14.backend.transaction.TransactionRepository;
import us.group14.backend.transaction.TransactionRequest;
import us.group14.backend.transaction.TransactionType;
import us.group14.backend.user.User;
import us.group14.backend.user.UserDetailsService;
import us.group14.backend.user.UserRole;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(userDetailsService, transactionRepository, accountRepository);
    }

    @Test
    void getAccount() {
        User user = new User("fred", "fred@email.com", "password", UserRole.USER);
        user.setId(1L);
        Account account = new Account();
        account.setId(1L);
        user.setAccount(account);
        account.setUser(user);

        given(accountRepository.getAccountForUser(anyLong())).willReturn(account);

        ResponseEntity<Account> response = accountService.getAccount(user);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(accountRepository).getAccountForUser(captor.capture());
        assertThat(captor.getValue()).isEqualTo(user.getId());

        assertThat(response.getBody()).isEqualTo(account);
    }

    @Test
    void getBalance() {
        User user = new User("fred", "fred@email.com", "password", UserRole.USER);
        user.setId(1L);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(10d);
        user.setAccount(account);
        account.setUser(user);

        ResponseEntity<Double> response = accountService.getBalance(user);
        assertThat(response.getBody()).isEqualTo(10d);
    }

    @Test
    void withdrawShouldCreateANewWithdrawTransactionWithTheCorrectAmountAndShouldSetTheAccountBalanceToAnUpdatedValueAndShouldCallTheNeededSaveMethodsAndReturnAnOKApiResponse() {
        User user = new User("fred", "fred@email.com", "password", UserRole.USER);
        user.setId(1L);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(10d);
        user.setAccount(account);
        account.setUser(user);

        when(transactionRepository.save(any())).then((Answer<Transaction>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Transaction) args[0];
        });

        TransactionRequest request = new TransactionRequest(5d, null, null);
        ResponseEntity<String> response = accountService.withdraw(user, request);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(2)).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getAllValues().get(0).getAmount()).isEqualTo(request.getAmount());
        assertThat(transactionCaptor.getAllValues().get(0).getType()).isEqualTo(TransactionType.WITHDRAW);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        assertThat(accountCaptor.getValue()).isEqualTo(account);

        assertThat(account.getBalance()).isEqualTo(5d);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void deposit() {

    }

    @Test
    void transfer() {
    }
}