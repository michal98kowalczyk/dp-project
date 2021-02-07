package dp.orm.mapper;


import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.DatabaseSchema;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.NameUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SingleInheritanceMapper  extends InheritanceMapper{

    private DatabaseSchema databaseSchema;

    public SingleInheritanceMapper(DatabaseSchema databaseSchema){
        this.databaseSchema = databaseSchema;
    }




    @Override
    public <T> InheritanceMapping map(Class<T> cls) {

        Class<? super T > parentClass = cls.getSuperclass();
        InheritanceMapping inheritanceMapping;

        if (parentClass.equals(Object.class)){

            List<Field> fields = filterFields(new ArrayList<>(Arrays.asList(cls.getDeclaredFields())));


            TableSchema tableSchema = buildTableSchema(cls,fields);
            Map<String,TableSchema> mapping = buildMapping(fields,tableSchema);

            databaseSchema.addMapping(cls,new InheritanceMapping(mapping));
            return new InheritanceMapping(mapping);
        }else {

            inheritanceMapping = databaseSchema.getMapping(parentClass);

            if (inheritanceMapping == null){
                inheritanceMapping = this.map(parentClass);

                databaseSchema.addMapping(cls,inheritanceMapping);
            }

            List<Field> fields = filterFields(new ArrayList<>(Arrays.asList(cls.getDeclaredFields())));

            TableSchema tableSchema = inheritanceMapping.getTableSchema(parentClass.getFields()[0].getName());
            tableSchema.addColumns(createColumns(fields));

            Map<String,TableSchema> mapping = buildMapping(fields,tableSchema);

            inheritanceMapping.union(mapping);

            databaseSchema.addMapping(cls,inheritanceMapping);


        }


        return inheritanceMapping;

    }

    private  List<Field> filterFields(List<Field> fields) {
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
