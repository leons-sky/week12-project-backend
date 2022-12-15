package us.group14.backend.account;

import jakarta.persistence.*;
import us.group14.backend.transaction.Transaction;
import us.group14.backend.user.User;

import java.util.Set;

@Entity
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
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Double balance;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "Transaction")
    private Set<Transaction> transactions;

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction>transactions) {
        transactions = transactions;
    }

   public void withdraw (double amount) {
        this.balance = this.balance - amount;
   }

    public void deposit (double amount) {
        this.balance = this.balance + amount;
    }
}
