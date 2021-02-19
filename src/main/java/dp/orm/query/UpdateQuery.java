package dp.orm.query;

import dp.orm.Dao;
import dp.orm.OrmManager;
import dp.orm.annotations.*;
import dp.orm.exceptions.NullableFieldException;
import dp.orm.exceptions.UpdateException;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class UpdateQuery extends QueryBuilder{

    private InheritanceMapping inheritanceMapping;
    Map<TableSchema,StringBuilder> tableSchemaStringBuilderMap;
    Set<TableSchema> tableSchemaSet;
    Object object;
    List<Field> fields;

    public UpdateQuery(InheritanceMapping inheritanceMapping){
        this.inheritanceMapping = inheritanceMapping;
        tableSchemaStringBuilderMap = new HashMap<>();
        tableSchemaSet = new HashSet<>();
        this.fields = new LinkedList<>();
        this.query = new StringBuilder();
        this.subQuery = new StringBuilder();
    }

    @Override
    <T> QueryBuilder withObject(T object) {
        this.object=object;
        fields = FieldUtils.getAllFields(object.getClass());
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
            tableSchemaStringBuilderMap.get(tableSchema).append("UPDATE ");
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
        tableSchemaSet.forEach(tableSchema -> {
            tableSchemaStringBuilderMap.get(tableSchema).append(" SET  ");
        });
        Object tmpObj;

//        System.out.println(tableSchema.getColumns().size());
        //moze wiecie jak inaczej to obsluzyc
        for (TableSchema tableSchema : tableSchemaSet) {
            StringBuilder stringBuilder = tableSchemaStringBuilderMap.get(tableSchema);


            for (ColumnSchema columnSchema : tableSchema.getColumns()) {

                if (columnSchema.getColumnName().equals(tableSchema.getId().getColumnName()) && columnSchema.isGeneratedId()) {
                    continue;
                }


                stringBuilder.append(columnSchema.getColumnName()).append(" = ");
                Class cls = columnSchema.getJavaType();
                Object obj;

                try {
                    obj = columnSchema.get(object);


                    if (obj == null) {
                        stringBuilder.append(parseNullableField(object, columnSchema));
                    } else if (obj.getClass() == String.class) {
                        stringBuilder.append("'").append(cls.cast(obj).toString()).append("', ");

                    } else {
                        stringBuilder.append(obj.toString()).append(", ");
                    }
                } catch (Exception e) {
                    stringBuilder.append("NULL, ");

                }

            }

//        query.delete(query.length() - 2, query.length() - 1);
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() -  1);


        }


        return this;
    }

    @Override
    QueryBuilder setValues() throws InvocationTargetException, IllegalAccessException {


        return this;

    }
    private <T> String parseNullableField(T object, ColumnSchema columnSchema) {
        if (columnSchema.isNullable()){
            return "NULL, ";
        }
        throw new NullableFieldException("This object is a null! Error! " + object.toString());
    }

    @Override
    QueryBuilder withCondition(int id, boolean isConditionSet) {

        checkAnnotations(fields,id);

        Optional<Field> foreignKey = getforeignKeyIfExists(fields);


        if(foreignKey.isPresent()){
            updateByForeignKey(foreignKey.get(),id);
        }else {
            tableSchemaSet.forEach(tableSchema -> {
                String columnName = tableSchema.getId().getColumnName();
                tableSchemaStringBuilderMap.get(tableSchema).append(" WHERE ").append(columnName).append(" = ").append(id).append(";");
            });
        }



        return this;
    }
    private Optional<Field> getforeignKeyIfExists(List<Field> fields) {

        return fields.stream().filter(field -> field.isAnnotationPresent(ForeignKey.class)).findAny();

    }


    private void checkAnnotations(List<Field> fields, int id) {

        fields.forEach(field -> {
            if (field.isAnnotationPresent(OneToOne.class) ){

                Class<?> clazz = object.getClass();
                Field ff = null; //Note, this can throw an exception if the field doesn't exist.
                try {
                    ff = clazz.getField(field.getName());
                    Object fieldValue = ff.get(object);

                    createSubQueryFromObject(fieldValue, id);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }



            }
            if (field.isAnnotationPresent(OneToMany.class) ){

                Class<?> clazz = object.getClass();
                Field ff = null; //Note, this can throw an exception if the field doesn't exist.

                try {
                    ff = clazz.getField(field.getName());
                    Object fieldValue = ff.get(object);

//                    List<Field> items = new ArrayList<Field>((Collection<? extends Field>) fieldValue);
//                    items.forEach(System.out::println);

                    List items = (ArrayList)fieldValue;
                    items.forEach(item ->{
//                        System.out.println(item);
//                        System.out.println(item.getClass());
                        createSubQueryFromObject(item, id);
                    });
//                    List<Field> list = new ArrayList<Field>(Arrays.asList(fieldValue));




                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }

//
//                    System.out.println("typ " + field.getType());
//
//                    createSubQuery(field);


            }

        });
    }
    private void updateByForeignKey(Field field, int id) {

        String name = field.getAnnotation(ForeignKey.class).name();


        tableSchemaSet.forEach(tableSchema -> {
            tableSchemaStringBuilderMap.get(tableSchema).append(" WHERE ").append(name).append(" = ").append(id).append(";");
        });



    }

    private void createSubQueryFromObject(Object item, int id) {


        try {
            Dao<Object> dao = OrmManager.getDao((Class<Object>) item.getClass());
            InheritanceMapping mapping = dao.getMapping();
            QueryBuilder queryBuilder = new UpdateQuery(mapping);
            QueryDirector<Object> queryDirector = new QueryDirector<>(queryBuilder);
            String queryTmp = queryDirector.withObject(item).withCondition(id).build();
            subQuery.append(queryTmp).append(" ");
            System.out.println("subQuery " + subQuery);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    QueryBuilder compose() {
//        System.out.println(subQuery);
        query.append(subQuery.toString());
        tableSchemaSet.forEach(tableSchema -> {
            query.append(tableSchemaStringBuilderMap.get(tableSchema).toString()).append(" ");
        });


        return this;
    }
}
