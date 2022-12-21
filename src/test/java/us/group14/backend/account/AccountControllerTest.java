package us.group14.backend.account;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import us.group14.backend.PayApplication;
import us.group14.backend.transaction.TransactionService;
import us.group14.backend.user.AuthUser;
import us.group14.backend.user.User;
import us.group14.backend.user.UserRole;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest(classes = PayApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class AccountControllerTest {

    @MockBean
    private AccountService accountService;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private AuthUser authUser;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AccountController accountController;

//    public ResponseEntity<Account> getAccount(AuthUser authUser) {
//        User user = authUser.get();
//        return accountService.getAccount(user);
//    }

    @Test
    void getAccount() throws Exception {

    }

    @Test
    void getBalance() throws Exception {
        User user = new User("user1", "user1@mail.com", "password", UserRole.USER);
        Account account = new Account();

        when(authUser.get()).thenReturn(user);
        when(accountService.getBalance(any())).thenReturn(ResponseEntity.ok(account.getBalance()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/balance"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("0.0"));
    }

    @Test
    void withdraw() {
    }

    @Test
    void deposit() {
    }

    @Test
    void transfer() {
    }

    @Test
    void getTransactions() {
    }

    @Test
    void getTransaction() {
    }
}