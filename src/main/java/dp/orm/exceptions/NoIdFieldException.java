package dp.orm.exceptions;

public class NoIdFieldException extends RuntimeException {
    public NoIdFieldException(String msg) {
        super(msg);
    }

    public NoIdFieldException() {
    }
}