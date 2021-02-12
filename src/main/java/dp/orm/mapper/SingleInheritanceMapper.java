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
        System.out.println("mapping dla : "+cls.getName() + " parent to " + cls.getSuperclass());

        Class<? super T > parentClass = cls.getSuperclass();
        InheritanceMapping inheritanceMapping;

        if (parentClass.equals(Object.class)){

            List<Field> fields = filterFields(new ArrayList<>(Arrays.asList(cls.getDeclaredFields())));
            System.out.println("Pola to "+fields);


            TableSchema tableSchema = buildTableSchema(cls,fields);
            final Map<String,TableSchema> mapping = buildMapping(fields,tableSchema);

//            inheritanceMapping = new InheritanceMapping(mapping);



            return databaseSchema.addMapping(cls,new InheritanceMapping(mapping));

        }else {


            inheritanceMapping = databaseSchema.getMapping(parentClass);


            if (inheritanceMapping == null){
                inheritanceMapping = this.map(parentClass);

                databaseSchema.addMapping(cls,inheritanceMapping);
            }
            System.out.println("Mapping rodzica "+inheritanceMapping);
            List<Field> fields = filterFields(new ArrayList<>(Arrays.asList(cls.getDeclaredFields())));
            System.out.println("Pola to "+fields);
//            System.out.println("Pola ktore nie  dzialaja W singleIM :"+cls.getName() + " parent " + parentClass.getName() );
//            for(var i : parentClass.getFields()){
//                System.out.println(i);
//            }

            TableSchema tableSchema = inheritanceMapping.getTableSchema(parentClass.getFields()[0].getName());
            System.out.println("table schema przed  "+tableSchema.getColumns().size());
            System.out.println("dodane kolumny  "+createColumns(fields));
            tableSchema.addColumns(createColumns(fields));

            System.out.println("table schema po dodaniu  "+tableSchema.getColumns().size());

            final Map<String,TableSchema> mapping = buildMapping(fields,tableSchema);

            inheritanceMapping.union(mapping);


//            databaseSchema.addMapping(cls,inheritanceMapping);
//
//
//            return inheritanceMapping;

                return databaseSchema.addMapping(cls,inheritanceMapping);
        }

//        databaseSchema.addMapping(cls,inheritanceMapping);
//
//
//        return inheritanceMapping;


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




