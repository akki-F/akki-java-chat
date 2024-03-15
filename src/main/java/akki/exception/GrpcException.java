package akki.exception;

import java.io.IOException;

/**
 * アプリケーションの基底例外<br/>
 */
public class GrpcException extends IOException {

    private String message;

    private String[] details;

    public GrpcException(Throwable cause) {
        this(cause, "systemError");
    }

    public GrpcException(String message, String... details) {
        this((Throwable) null, message, details);
    }

    public GrpcException(Throwable cause, String message, String... details) {
        super(message, cause);
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return this.message;
    }

    public String[] getDetails() {
        return this.details;
    }

}
