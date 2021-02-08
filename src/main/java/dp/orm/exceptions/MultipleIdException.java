package dp.orm.exceptions;

public class MultipleIdException extends RuntimeException {
    public MultipleIdException(String msg) {
        super(msg);
    }

    public MultipleIdException() {
    }
}