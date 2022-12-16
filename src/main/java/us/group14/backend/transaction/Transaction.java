package us.group14.backend.transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import us.group14.backend.account.Account;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_sequence",
            sequenceName = "transaction_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private Double amount;

    private LocalDateTime transferredAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id")
    private Account recipient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private Account sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private String message;

    public Transaction(Double amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
    }

    public Transaction(Double amount, TransactionType type, String message) {
        this.amount = amount;
        this.type = type;
        this.message = message;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(Double amount) {
//        this.amount = amount;
//    }
//
//    public LocalDateTime getTransferredAt() {
//        return transferredAt;
//    }
//
//    public void setTransferredAt(LocalDateTime transferredAt) {
//        this.transferredAt = transferredAt;
//    }
//
//    public Account getRecipient() {
//        return recipient;
//    }
//
//    public void setRecipient(Account recipient) {
//        this.recipient = recipient;
//    }
//
//    public Account getSender() {
//        return sender;
//    }
//
//    public void setSender(Account sender) {
//        this.sender = sender;
//    }
//
//    public TransactionType getType() {
//        return type;
//    }
//
//    public void setType(TransactionType type) {
//        this.type = type;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public void complete(Account account) {
        switch (getType()) {
            case DEPOSIT -> {
                account.deposit(getAmount());
                setRecipient(account);
                setTransferredAt(LocalDateTime.now());
            }
            case WITHDRAW -> {
                account.withdraw(getAmount());
                setSender(account);
                setTransferredAt(LocalDateTime.now());
            }
        }
    }

    public void complete(Account recipient, Account sender) {
        switch (getType()) {
            case TRANSFER -> {
                //Sending money
                sender.withdraw(getAmount());

                //Receiving money
                recipient.deposit(getAmount());

                setSender(sender);
                setRecipient(recipient);
                setTransferredAt(LocalDateTime.now());
            }
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", transferredAt=" + transferredAt +
                ", recipient=" + (recipient == null ? null : recipient.getId()) +
                ", sender=" + (sender == null ? null : sender.getId()) +
                ", type=" + type +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transaction that = (Transaction) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
