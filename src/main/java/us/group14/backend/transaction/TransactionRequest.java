package us.group14.backend.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TransactionRequest {

    private final Double amount;
    private final String message;
    private final Long recipientId;

}
