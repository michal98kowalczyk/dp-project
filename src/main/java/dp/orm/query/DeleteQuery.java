package dp.orm.query;

import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DeleteQuery extends QueryBuilder {

    private InheritanceMapping inheritanceMapping;
    Map<TableSchema,StringBuilder> tableSchemaStringBuilderMap;
    Set<TableSchema> tableSchemaSet;
    Object object;
    List<Field> fields;

    public DeleteQuery(InheritanceMapping inheritanceMapping){
        this.inheritanceMapping = inheritanceMapping;
        tableSchemaStringBuilderMap = new HashMap<>();
        tableSchemaSet = new HashSet<>();
        this.fields = new LinkedList<>();
        this.query = new StringBuilder();
    }

    @Override
    <T> QueryBuilder withObject(T object) {
        this.object=object;
        fields = FieldUtils.getAllFields((Class)object);
        return this;
    }

    @Override
    QueryBuilder createOperation() {

        fields.forEach(field -> {
            TableSchema tableSchema = inheritanceMapping.getTableSchema(field.getName());
            tableSchemaStringBuilderMap.put(tableSchema,new StringBuilder());
            tableSchemaSet.add(tableSchema);
        });

        tableSchemaSet.forEach(tableSchema -> {
            tableSchemaStringBuilderMap.get(tableSchema).append("DELETE FROM ");
        });

        return this;
    }

    @Override
    QueryBuilder setTable() {

        tableSchemaSet.forEach(tableSchema -> {
            tableSchemaStringBuilderMap.get(tableSchema).append(tableSchema.getName());
        });


        return this;
    }

    @Override
    QueryBuilder setFields() {
        return this;
    }

    @Override
    QueryBuilder setValues() throws InvocationTargetException, IllegalAccessException {
        return this;
    }

    @Override
    QueryBuilder withCondition(int id, boolean isConditionSet) {

        tableSchemaSet.forEach(tableSchema -> {
            String columnName = tableSchema.getId().getColumnName();
            tableSchemaStringBuilderMap.get(tableSchema).append(" WHERE ").append(columnName).append(" = ").append(id).append(";");
        });


        return this;
    }

    @Override
    QueryBuilder compose() {

        tableSchemaSet.forEach(tableSchema -> {
            query.append(tableSchemaStringBuilderMap.get(tableSchema).toString()).append(" ");
        });


        return this;
    }
}
