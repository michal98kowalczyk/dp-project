package dp.orm.exceptions;

public class DeleteException extends RuntimeException{
    public DeleteException(String msg,Throwable cause){
        super(msg,cause);
    }
}
