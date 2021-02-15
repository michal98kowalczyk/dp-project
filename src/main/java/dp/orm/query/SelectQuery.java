package dp.orm.query;

import dp.orm.JoinColumn.JoinColumnEntity;
import dp.orm.annotations.Id;
import dp.orm.annotations.JoinColumn;
import dp.orm.annotations.OneToMany;
import dp.orm.annotations.OneToOne;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.FieldUtils;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SelectQuery extends QueryBuilder {

    private InheritanceMapping inheritanceMapping;
    Object object;
    Field field;
    TableSchema tableSchema;
    Set<TableSchema> setTableSchema;
    List<Field> fields;
    Optional<JoinColumnEntity> joinColumnEntityForOneToOne = Optional.empty();
    Optional<JoinColumnEntity> joinColumnEntityForOneToMany = Optional.empty();

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
            if(field.isAnnotationPresent(OneToOne.class)) {
                this.joinColumnEntityForOneToOne = Optional.of(makeColumnSchemaForJoinColumnAnnotation(field));
            }
            if(field.isAnnotationPresent(OneToMany.class)) {
                this.joinColumnEntityForOneToMany = Optional.of(makeColumnSchemaForJoinColumnAnnotation(field));
            }
        }

        for (TableSchema schema : setTableSchema) {
            for (ColumnSchema colSchema : schema.getColumns()) {
                System.out.println(colSchema);
                query.append(schema.getName())
                        .append(".")
                        .append(colSchema.getColumnName())
                        .append(", ");
                if(colSchema.isJoinColumnAnnotationPresent()) {
                    isJoinColumnAnnotationPresent = true;
                    columnSchemaToJoin = colSchema;
                }
            }
        }

        query.delete(query.length() - 2, query.length() - 1)
                .append(" FROM ");
        return this;
    }

    private JoinColumnEntity makeColumnSchemaForJoinColumnAnnotation(Field field) {
        return new JoinColumnEntity(field.getAnnotation(JoinColumn.class).name(),
                field.getAnnotation(JoinColumn.class).referencedColumnName(),
                field.getAnnotation(JoinColumn.class).referencedClass().toString());
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

        System.out.println(isJoinColumnAnnotationPresent);
        if(isJoinColumnAnnotationPresent) {
            JoinColumnEntity  joinColumnEntity = columnSchemaToJoin.getJoinColumnEntity();
            query.append("JOIN ")
                    .append(columnSchemaToJoin.getClass())
                    .append(" ON ")
                    .append(tableSchema.getCls().toString() + "." + tableSchema.getId().getColumnName())
                    .append(" = ")
                    .append(joinColumnEntity.getReferencedClass() + "." + joinColumnEntity.getReferencedColumnName())
                    .append(" ");
        }

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
