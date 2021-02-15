package dp.orm.query;

import java.lang.reflect.InvocationTargetException;

public class QueryDirector<T> {

    private QueryBuilder queryBuilder;

    private T object;

    private int condition=0;

    private boolean isConditionSet=false;

    public QueryDirector(QueryBuilder queryBuilder){
        this.queryBuilder=queryBuilder;

    }

    public QueryDirector withCondition(int id){
        this.condition =id;
        this.isConditionSet=true;

        return this;
    }

    public QueryDirector withObject(T object){
        this.object = object;

        return this;
    }

    public String build() throws InvocationTargetException, IllegalAccessException {

        return queryBuilder
                .withObject(object)
                .createOperation()
                .setTable()
                .setFields()
                .setValues()
                .withCondition(condition,isConditionSet)
                .compose().generate();
    }


}
