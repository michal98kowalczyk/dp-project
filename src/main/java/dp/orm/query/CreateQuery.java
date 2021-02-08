package dp.orm.query;

import dp.orm.exceptions.ObjectClassException;
import dp.orm.schemas.TableSchema;

import java.lang.reflect.InvocationTargetException;

public class CreateQuery extends QueryBuilder{

    private Object object;

    public CreateQuery(){
        query = new StringBuilder();
    }

    @Override
    <T> QueryBuilder withObject(T object) {
        if (object.getClass() != TableSchema.class) throw new ObjectClassException("Wrong class " + object.getClass().toString() );

        this.object = object;
        return this;
    }

    @Override
    QueryBuilder createOperation() {
        query.append("CREATE TABLE IF NOT EXISTS ");

        return this;
    }

    @Override
    QueryBuilder setTable() {
        query.append(((TableSchema)object).getName()).append(" (");

        return this;
    }

    @Override
    QueryBuilder setFields() {

        ((TableSchema)object).getColumns().forEach(columnSchema -> {

            query.append("\t");
            query.append(columnSchema.getColumnName());
            query.append(" ");


            if (columnSchema.isGeneratedId()){
                query.append("SERIAL");
            }else {
                query.append(columnSchema.getSqlType()=="VARCHAR"  ? "TEXT" : columnSchema.getSqlType());
            }


            if (!columnSchema.isNullable()){
                query.append(" ");
                query.append("NOT NULL");
            }
            query.append(",\n");




        });

        query.append("PRIMARY KEY(");
        query.append(((TableSchema)object).getId().getColumnName());
        query.append(")\n);");


        return this;
    }

    @Override
    QueryBuilder setValues() throws InvocationTargetException, IllegalAccessException {
        return this;
    }

    @Override
    QueryBuilder withCondition(int id, boolean isConditionSet) {
        return this;
    }

    @Override
    QueryBuilder compose() {
        return this;
    }
}
