package us.group14.backend.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@ToString
@AllArgsConstructor
public class TransactionRequest {

    private final Double amount;
    private final String message;
    private final Long recipientId;

}
