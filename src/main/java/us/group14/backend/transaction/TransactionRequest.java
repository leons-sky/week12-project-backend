package us.group14.backend.transaction;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@ToString
@AllArgsConstructor
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    @PositiveOrZero
    private final Double amount;

    @Length(max = 150)
    private final String message;

    private final String recipient;

}
