package us.group14.backend.account;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import us.group14.backend.transaction.Transaction;
import us.group14.backend.views.AccountView;
import us.group14.backend.views.AllUserInfoView;
import us.group14.backend.views.TransactionAndAccountView;
import us.group14.backend.user.User;
import us.group14.backend.views.UserAndAccountView;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    @JsonView({AccountView.class, UserAndAccountView.class, TransactionAndAccountView.class, AllUserInfoView.class})
    private Long id;

    @Column(nullable = false)
    @JsonView({AccountView.class, UserAndAccountView.class, TransactionAndAccountView.class, AllUserInfoView.class})
    private Double balance;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonView(TransactionAndAccountView.class)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    @OneToMany(fetch = FetchType.EAGER)
//    @JsonView(AllUserInfoView.class)
//    private Set<Transaction> transactions;

    public Account() {
        this.balance = 0d;
    }

    //    public Long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public Double getBalance() {
//        return balance;
//    }
//
//    public void setBalance(Double balance) {
//        this.balance = balance;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public Set<Transaction> getTransactions() {
//        return this.transactions;
//    }

//    public void addTransaction(Transaction transaction) {
//        this.transactions.add(transaction);
//    }

    public void withdraw(double amount) {
        this.balance = this.balance - amount;
        System.out.println("WITHDRAW " + this.balance);
   }

    public void deposit(double amount) {
        this.balance = this.balance + amount;
        System.out.println("DEPOSIT " + this.balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", user=" + user.getId() +
//                ", transactions=" + transactions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
