package dp.orm.exceptions;

public class InsertException extends RuntimeException {
    public InsertException(String msg,Throwable cause){
        super(msg,cause);
    }
}
