package dp.orm.query;

import java.lang.reflect.InvocationTargetException;

public class QueryBuildDirector<T> {

    private QueryBuilder queryBuilder;

    private T object;

    private int condition;

    private boolean isConditionSet;

    public QueryBuildDirector(QueryBuilder queryBuilder) {

        this.queryBuilder = queryBuilder;
        this.isConditionSet = false;
        this.condition = 0;
    }

    public QueryBuildDirector withObject(T object) {
        this.object = object;
        return this;
    }

    public QueryBuildDirector withCondition(int id) {
        this.condition = id;
        this.isConditionSet = true;
        return this;
    }

    public String build() throws InvocationTargetException, IllegalAccessException {
        return queryBuilder.withObject(object)
                .createOperation()
                .setTable()
                .setFields()
                .setValues()
                .withCondition(condition, isConditionSet)
                .compose()
                .generate();
    }
}
