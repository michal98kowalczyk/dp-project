package dp.orm.query;

import dp.orm.annotations.Id;
import dp.orm.exceptions.UpdateException;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpdateQuery extends QueryBuilder{

    private InheritanceMapping inheritanceMapping;
    TableSchema tableSchema;
    Object object;
    Field field;

    public UpdateQuery(InheritanceMapping inheritanceMapping){
        this.inheritanceMapping = inheritanceMapping;
        this.query=new StringBuilder();
    }

    @Override
    <T> QueryBuilder withObject(T object) {
        this.object=object;
        return this;
    }

    @Override
    QueryBuilder createOperation() {
        query.append("UPDATE ");

        return this;
    }

    @Override
    QueryBuilder setTable() {

        field = FieldUtils.getAllFields(object.getClass()).stream().filter(field -> field.isAnnotationPresent(Id.class)).findFirst()
                .orElseThrow(() -> new UpdateException("Error during update" + object.getClass().getName()));

        tableSchema = inheritanceMapping.getTableSchema(field.getName());
        query.append(tableSchema.getName());

        return this;
    }

    @Override
    QueryBuilder setFields() {
        query.append(" SET  (");
        Object tmpObj;

//        System.out.println(tableSchema.getColumns().size());
        //moze wiecie jak inaczej to obsluzyc



        for (ColumnSchema columnSchema : tableSchema.getColumns()) {

            if (tableSchema.getColumns().size() == 2){
                query.deleteCharAt(query.length()-1);
            }

            if (columnSchema.getColumnName().equals(tableSchema.getId().getColumnName()) && columnSchema.isGeneratedId() ) {
                continue;
            }

            try {
                tmpObj = columnSchema.get(object);
            } catch (Exception e) {
                continue;

            }

            query.append(columnSchema.getColumnName()).append(", ");
        }

        query.delete(query.length() - 2, query.length() - 1);

        if (tableSchema.getColumns().size() == 2){
            query.append(" = ");
        }else{
            query.append(") = (");
        }




        return this;
    }

    @Override
    QueryBuilder setValues() throws InvocationTargetException, IllegalAccessException {

        Object tmpObj;

        for (ColumnSchema columnSchema : tableSchema.getColumns()) {

            if (columnSchema.getColumnName().equals(tableSchema.getId().getColumnName()) && columnSchema.isGeneratedId() ) {
                continue;
            }

            try{
            tmpObj = columnSchema.get(object);

        }catch (Exception e){
                continue;
            }

        if (tmpObj == null){
            query.append("NULL,");
            continue;
        }

        if (tmpObj.getClass() == String.class) {
            query.append("\'").append(tmpObj).append("\'");

        }else {
            query.append(tmpObj);
        }


        query.append(",");



        }


        query.deleteCharAt(query.length()-1);


        return this;
    }

    @Override
    QueryBuilder withCondition(int id, boolean isConditionSet) {
        String columnName = tableSchema.getId().getColumnName();
//        Object obj = tableSchema.getId().get(object);

        if (tableSchema.getColumns().size() != 2){
            query.append(") ");
        }

        query.append(" WHERE ").append(columnName).append(" = ").append(id).append(";");

        return this;
    }

    @Override
    QueryBuilder compose() {
        return this;
    }
}
