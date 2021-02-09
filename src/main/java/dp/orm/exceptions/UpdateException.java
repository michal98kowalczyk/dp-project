package dp.orm.exceptions;

public class UpdateException extends RuntimeException {
    public UpdateException(String msg){
        super(msg);
    }
    public UpdateException(String msg,Throwable cause){
        super(msg,cause);
    }
}
