package dp.orm.mapper;

import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.DatabaseSchema;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.NameUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ConcreteInheritanceMapper extends InheritanceMapper {

    private DatabaseSchema databaseSchema;

    public ConcreteInheritanceMapper(DatabaseSchema databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    @Override
    public <T> InheritanceMapping map(Class<T> cls) {

        List<Field> fields = filterFields(cls);


        TableSchema tableSchema = buildTableSchema(cls,fields);



        final Map<String, TableSchema> columnMapping = buildMapping(fields,tableSchema);

        final var mapping = new InheritanceMapping(columnMapping);
        databaseSchema.addMapping(cls, mapping);
        return mapping;
    }

    private <T> List<Field> filterFields(Class<T> cls) {
        List<Field> fields = new ArrayList<>(Arrays.asList(cls.getDeclaredFields()));
        Class<? super T> parent = cls.getSuperclass();

        while (parent != null && !parent.equals(Object.class)) {
            fields.addAll(Arrays.asList(parent.getDeclaredFields()));
            parent = parent.getSuperclass();
        }

        return fields.stream().filter(field -> !field.getName().startsWith("this")).collect(Collectors.toList());
    }


    private <T> TableSchema buildTableSchema(Class<T> cls,List<Field> fields) {


        return TableSchema.builder().cls(cls).name(NameUtils.extractTableName(cls)).columns(createColumns(fields))
                .id(getId(fields)).build();
    }

    private Map<String, TableSchema> buildMapping(List<Field> fields,TableSchema tableSchema){
        Map<String,TableSchema> mapping = new HashMap<>();


        fields.stream().map(Field::getName).forEach(name -> mapping.put(name,tableSchema));

        return mapping;

    }
}