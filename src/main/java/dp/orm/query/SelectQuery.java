package dp.orm.query;

import dp.orm.annotations.Id;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectQuery extends QueryBuilder {

    private InheritanceMapping inheritanceMapping;
    Object object;
    Field field;
    TableSchema tableSchema;
    Set<TableSchema> setTableSchema;
    List<Field> fields;

    public SelectQuery(InheritanceMapping inheritanceMapping) {
        this.inheritanceMapping = inheritanceMapping;
        this.query = new StringBuilder();
        this.setTableSchema = new HashSet<>();
        this.fields = new LinkedList<>();
    }

    @Override
    <T> QueryBuilder withObject(T object) {
        this.object = object;
        return this;
    }

    @Override
    QueryBuilder createOperation() {
        query.append("SELECT ");

        fields = FieldUtils.getAllFields((Class<?>) object);
        for (Field field : fields) {
            setTableSchema.add(inheritanceMapping.getTableSchema(field.getName()));

        }

        for (TableSchema schema : setTableSchema) {
            for (ColumnSchema colSchema : schema.getColumns()) {
                query.append(schema.getName())
                        .append(".")
                        .append(colSchema.getColumnName())
                        .append(", ");
            }
        }

        query.delete(query.length() - 2, query.length() - 1)
                .append(" FROM ");
        return this;
    }

    @Override
    QueryBuilder setTable() {
        field = fields
                .stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList())
                .get(0);

        tableSchema = inheritanceMapping
                .getTableSchema(field.getName());

        query.append(tableSchema.getName())
                .append(" ");

        fields.remove(field);

        setTableSchema.clear();
        for (Field field : fields) {
            setTableSchema.add(inheritanceMapping.getTableSchema(field.getName()));
        }

        setTableSchema.remove(tableSchema);

        for (TableSchema schema : setTableSchema) {
            query.append("JOIN ")
                    .append(schema.getName())
                    .append(" ON ")
                    .append(tableSchema.getName() + "." + tableSchema.getId().getColumnName())
                    .append(" = ")
                    .append(schema.getName() + "." + schema.getId().getColumnName())
                    .append(" ");
        }

        return this;
    }

    @Override
    QueryBuilder setFields() {
        return this;
    }

    @Override
    QueryBuilder setValues() {
        return this;
    }

    @Override
    QueryBuilder withCondition(int id, boolean isConditionSet) {
        if (isConditionSet) {
            query.append(" WHERE ")
                    .append(tableSchema.getName())
                    .append(".")
                    .append(tableSchema.getId().getColumnName())
                    .append(" = ")
                    .append(id);
        }
        query.append(";");
        return this;
    }

    @Override
    QueryBuilder compose() {
        return this;
    }
}
