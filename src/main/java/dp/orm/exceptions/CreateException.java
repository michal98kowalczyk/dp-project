package dp.orm.exceptions;

import dp.orm.query.CreateQuery;

public class CreateException extends RuntimeException{
    public CreateException(String msg,Throwable cause){
        super(msg,cause);
    }
}
