package dp.orm.query;

import dp.orm.ForeignKey.ForeignKey;
import dp.orm.exceptions.ObjectClassException;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.TableSchema;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class CreateQuery extends QueryBuilder {

    private Object object;

    public CreateQuery() {
        query = new StringBuilder();
    }

    @Override
    <T> QueryBuilder withObject(T object) {
        if (object.getClass() != TableSchema.class)
            throw new ObjectClassException("Wrong class " + object.getClass().toString());

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
        query.append(((TableSchema) object).getName()).append(" (");

        return this;
    }

    @Override
    QueryBuilder setFields() {
        ((TableSchema) object).getColumns().forEach(columnSchema -> {
            query.append("\t");
            query.append(columnSchema.getColumnName());
            query.append(" ");
            if (columnSchema.isGeneratedId()) {
                query.append("SERIAL");
            } else {
                query.append(columnSchema.getSqlType() == "VARCHAR" ? "TEXT" : columnSchema.getSqlType());
            }
            if (!columnSchema.isNullable()) {
                query.append(" ");
                query.append("NOT NULL");
            }
            query.append(",\n");
        });

        query.append("PRIMARY KEY(");
        query.append(((TableSchema) object).getId().getColumnName());
        query.append(")");

        if (isForeignKeyInTableSchema(((TableSchema) object).getColumns())) {
            addForeignKeyToQuery(query, getForeignKeyFromTableSchema(((TableSchema) object).getColumns()));
        }
        query.append("\n);");
//        System.out.println(query);

        return this;
    }

    private boolean isForeignKeyInTableSchema(Set<ColumnSchema> columnSchemaSet) {
        return columnSchemaSet.stream()
                .anyMatch(ColumnSchema::isForeignKey);
    }

    private ForeignKey getForeignKeyFromTableSchema(Set<ColumnSchema> columnSchemaSet) {
        return columnSchemaSet.stream()
                .filter(ColumnSchema::isForeignKey)
                .map(ColumnSchema::getForeignKey)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private StringBuilder addForeignKeyToQuery(StringBuilder query, ForeignKey foreignKey) {
        return query.append(",\n")
                .append("FOREIGN KEY (")
                .append(foreignKey.getName())
                .append(") ")
                .append("REFERENCES ")
                .append(foreignKey.getReferencedClass())
                .append("(")
                .append(foreignKey.getReferencedField())
                .append(")");
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
