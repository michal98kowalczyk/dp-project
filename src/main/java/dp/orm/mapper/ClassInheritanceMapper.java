package dp.orm.mapper;

import dp.orm.mapping.ClassInheritanceMapping;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.DatabaseSchema;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.NameUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ClassInheritanceMapper extends InheritanceMapper {

    private final DatabaseSchema databaseSchema;

    public ClassInheritanceMapper(DatabaseSchema databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    @Override
    public <T> InheritanceMapping map(Class<T> cls) {
        Class<? super T> parentClass = cls.getSuperclass();

        if (cls.equals(Object.class)) {
            return new InheritanceMapping(Collections.emptyMap());
        }

        InheritanceMapping parentMapping = databaseSchema.getMapping(parentClass);
        if (parentMapping == null) {
            parentMapping = this.map(parentClass);
            databaseSchema.addMapping(parentClass, parentMapping);
        }
        List<Field> fields = filterFields(Arrays.asList(cls.getDeclaredFields()));

        Class<? super T> root = cls;
        while (!root.getSuperclass()
                .equals(Object.class)) {
            root = root.getSuperclass();
        }
        ColumnSchema rootIdField = getId(Arrays.asList(root.getDeclaredFields()));

//        System.out.println("RootIDFIELD: " + rootIdField);

        Set<ColumnSchema> columnSchemas = createColumns(fields);
        if (!columnSchemas.contains(rootIdField)) {
            columnSchemas.add(rootIdField);
        }


        TableSchema tableSchema = buildTableSchema(cls, fields);


        final Map<String, TableSchema> columnMapping = buildMapping(fields, tableSchema);

        final var mapping = new ClassInheritanceMapping(columnMapping, parentMapping);
        databaseSchema.addMapping(cls, mapping);
        return mapping;

    }

    private List<Field> filterFields(List<Field> fields) {
        return fields.stream().filter(field -> !field.getName().startsWith("this")).collect(Collectors.toList());
    }

    private <T> TableSchema buildTableSchema(Class<T> cls, List<Field> fields) {

        return TableSchema.builder().cls(cls).name(NameUtils.extractTableName(cls)).columns(createColumns(fields))
                .id(getId(fields)).build();
    }

    private Map<String, TableSchema> buildMapping(List<Field> fields, TableSchema tableSchema) {
        Map<String, TableSchema> mapping = new HashMap<>();


        fields.stream().map(Field::getName).forEach(name -> mapping.put(name, tableSchema));

        return mapping;

    }

}