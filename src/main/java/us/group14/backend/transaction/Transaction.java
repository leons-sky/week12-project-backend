package us.group14.backend.transaction;

import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.bouncycastle.cms.Recipient;
import us.group14.backend.account.Account;
import us.group14.backend.user.User;

import java.time.LocalDateTime;

@Entity
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private LocalDateTime transferredAt;

    public LocalDateTime getTransferredAt() {
        return transferredAt;
    }

    public void setTransferredAt(LocalDateTime transferredAt) {
        this.transferredAt = transferredAt;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "recipient")
    private Account recipient;

    public Account getRecipient() {
        return recipient;
    }

    public void setRecipient(Account recipient) {
        this.recipient = recipient;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "sender")
    private Account sender;

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

    private TransactionType type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
