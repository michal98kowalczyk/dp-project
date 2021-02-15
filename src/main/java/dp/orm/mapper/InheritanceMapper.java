package dp.orm.mapper;

import dp.orm.ForeignKey.ForeignKeyEntity;
import dp.orm.JoinColumn.JoinColumnEntity;
import dp.orm.annotations.*;
import dp.orm.exceptions.MultipleIdException;
import dp.orm.exceptions.NoIdFieldException;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class InheritanceMapper {

    public abstract <T> InheritanceMapping map(Class<T> cls);

    public ColumnSchema getId(List<Field> fields) {

        List<Field> idArray = fields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());

        if (idArray.size() > 1) {
            throw new MultipleIdException("mutliple ids");
        } else if (idArray.isEmpty()) {
            throw new NoIdFieldException("no id field ");
        }

        return new ColumnSchema(idArray.get(0));

    }

    public Set<ColumnSchema> createColumns(List<Field> fields) {
        return fields.stream()
                .filter(field -> field.isAnnotationPresent(DatabaseField.class))
                .filter(field -> isNotOneToOneOrOneToManyField(field))
                .map(field -> makeColumnSchema(field))
                .collect(Collectors.toSet());
    }

    private boolean isNotOneToOneOrOneToManyField(Field field) {
        if (field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(OneToMany.class)) {
            return false;
        }
        return true;
    }

    private ColumnSchema makeColumnSchema(Field field) {
        if (field.isAnnotationPresent(ForeignKey.class)) {
            return makeColumnSchemaForForeignKeyAnnotation(field);
        }
        if(field.isAnnotationPresent(JoinColumn.class)) {
            return makeColumnSchemaForJoinColumnAnnotation(field);
        }
        return new ColumnSchema(field);
    }

    private ColumnSchema makeColumnSchemaForForeignKeyAnnotation (Field field) {
        ColumnSchema columnSchema = new ColumnSchema(field);
        ForeignKeyEntity foreignKey = new ForeignKeyEntity(field.getAnnotation(ForeignKey.class).name(),
                field.getAnnotation(ForeignKey.class).referencedClass(), field.getAnnotation(ForeignKey.class).referencedField());
        columnSchema.setForeignKey(foreignKey);
        return columnSchema;
    }

    private ColumnSchema makeColumnSchemaForJoinColumnAnnotation(Field field) {
        ColumnSchema columnSchema = new ColumnSchema(field);
        JoinColumnEntity joinColumnEntity = new JoinColumnEntity(field.getAnnotation(JoinColumn.class).name(),
                field.getAnnotation(JoinColumn.class).referencedColumnName(),
                field.getAnnotation(JoinColumn.class).referencedClass().toString());
        columnSchema.setJoinColumn(joinColumnEntity);
        return columnSchema;
    }
}
