package dp.orm.query;

import dp.orm.schemas.DatabaseSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;



public abstract class QueryBuilder {



    protected StringBuilder query;

    abstract <T> QueryBuilder withObject(T object);

    abstract QueryBuilder createOperation();

    abstract QueryBuilder setTable();

    abstract QueryBuilder setFields();

    abstract QueryBuilder setValues() throws InvocationTargetException, IllegalAccessException;

    abstract QueryBuilder withCondition(int id, boolean isConditionSet);

    abstract QueryBuilder compose();

    String generate() {
        return query.toString();
    }



}
