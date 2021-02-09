package dp.orm.exceptions;

public class CouldNotSelectFromTableException extends RuntimeException {
    public CouldNotSelectFromTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
