package us.group14.backend.transaction;

import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import lombok.NoArgsConstructor;
import org.bouncycastle.cms.Recipient;
import us.group14.backend.account.Account;
import us.group14.backend.user.User;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
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
    @Column(nullable = false)
    private LocalDateTime transferredAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "recipient")
    private Account recipient;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "sender")
    private Account sender;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    private String message;

    public Transaction(Double amount, Account sender, TransactionType type) {
        this.amount = amount;
        this.sender = sender;
        this.type = type;
        this.transferredAt = LocalDateTime.now();
    }

    public Transaction(Double amount, Account sender, Account recipient, TransactionType type, String message) {
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.message = message;
        this.transferredAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransferredAt() {
        return transferredAt;
    }

    public void setTransferredAt(LocalDateTime transferredAt) {
        this.transferredAt = transferredAt;
    }

    public Account getRecipient() {
        return recipient;
    }

    public void setRecipient(Account recipient) {
        this.recipient = recipient;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
