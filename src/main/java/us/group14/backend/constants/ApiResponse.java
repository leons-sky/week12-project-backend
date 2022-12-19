package us.group14.backend.constants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum ApiResponse {
    OK(HttpStatus.OK, "OK"),
    EXCEEDS_BALANCE(HttpStatus.BAD_REQUEST, "Amount exceeds balance"),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "Amount is not valid"),
    INVALID_LIMIT(HttpStatus.BAD_REQUEST, "Invalid limit, must be [0, 100]"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "Email is not a valid email"),
    ALREADY_CONFIRMED_EMAIL(HttpStatus.IM_USED, "Email already confirmed"),
    CONFIRMATION_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Confirmation token not found"),
    CONFIRMATION_TOKEN_EXPIRED(HttpStatus.GONE, "Confirmation token has expired"),
    USER_TAKEN(HttpStatus.CONFLICT, "Username already taken"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "Invalid username or password"),
    USER_DISABLED(HttpStatus.CONFLICT, "User disabled"),
    USER_LOCKED(HttpStatus.FORBIDDEN, "User locked"),
    CONTACT_NOT_FOUND(HttpStatus.NOT_FOUND, "Contact not found"),
    CONTACT_ALREADY_ADDED(HttpStatus.BAD_REQUEST, "Contact already added");

    private final HttpStatus status;
    private final String message;

    ApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseEntity<String> toResponse() {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(this.status);
        if (this.message != null) {
            return builder.body(this.message);
        }
        return builder.build();
    }

    public HttpStatus status() {
        return this.status;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
