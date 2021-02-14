package dp.orm.query;

import dp.orm.DatabaseCreator;
import dp.orm.annotations.OneToMany;
import dp.orm.annotations.OneToOne;
import dp.orm.exceptions.InsertException;
import dp.orm.exceptions.NullableFieldException;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.DatabaseSchema;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class InsertQuery extends QueryBuilder {


    private DatabaseSchema databaseSchema;

    private InheritanceMapping inheritanceMapping;
    Map<TableSchema,StringBuilder> tableSchemaStringBuilderMap;
    Set<TableSchema> tableSchemaSet;
    Object object;
    List<Field> fields;
    String subQuery="";


    public InsertQuery(InheritanceMapping inheritanceMapping) {
        this.inheritanceMapping = inheritanceMapping;
        this.query = new StringBuilder();
        this.fields = new LinkedList<>();
        this.tableSchemaSet = new HashSet<>();
        this.tableSchemaStringBuilderMap = new HashMap<>();
        this.databaseSchema = new DatabaseSchema();

    }

    @Override
    <T> QueryBuilder withObject(T object) {

        this.object = object;
        fields = FieldUtils.getAllFields(object.getClass());
        System.out.println("obiekt "+object);
//        fields.forEach(System.out::println);
        checkAnnotations(fields);

        return this;

    }

    private void checkAnnotations(List<Field> fields) {

        fields.forEach(field -> {
            if (field.isAnnotationPresent(OneToOne.class) ){

                System.out.println(field.getType());
                System.out.println(field);
                createSubQuery(field);
            }
            if (field.isAnnotationPresent(OneToMany.class) ){

            }

        });

    }

    private void createSubQuery(Field field) {





        InheritanceMapping mapping = databaseSchema.getMapping(field.getType());

        QueryBuilder queryBuilder = new InsertQuery(mapping);

        QueryDirector<Object> queryDirector = new QueryDirector<>(queryBuilder);


        String queryTmp;

        try {
            System.out.println("Typ "+field.getAnnotatedType());
            System.out.println(field);
//            Object obj1 =field.getClass().getDeclaringClass();
            Object obj1 = object.getClass().getField(field.getName());
            queryTmp = queryDirector.withObject(obj1).build();
            System.out.println(queryTmp);
        } catch (InvocationTargetException e) {
            throw new InsertException("Error during insertion "+object.toString(),e);
        } catch (IllegalAccessException e) {
            throw new InsertException("Error during insertion "+object.toString(),e);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }


    @Override
    QueryBuilder createOperation() {


        fields.forEach(field -> {
            TableSchema tableSchema = inheritanceMapping.getTableSchema(field.getName());
            tableSchemaStringBuilderMap.put(tableSchema,new StringBuilder());
            tableSchemaSet.add(tableSchema);

        });


        tableSchemaSet.forEach(tableSchema -> tableSchemaStringBuilderMap.get(tableSchema).append("INSERT INTO "));

        return this;



    }

    @Override
    QueryBuilder setTable() {


        tableSchemaSet.forEach(tableSchema -> tableSchemaStringBuilderMap.get(tableSchema).append(tableSchema.getName())
                .append("("));

        return this;
    }

    @Override
    QueryBuilder setFields() {

//        tableSchemaSet.forEach(tableSchema -> {
//            StringBuilder stringBuilder = tableSchemaStringBuilderMap.get(tableSchema);
//            tableSchema.getColumns().forEach(columnSchema -> {
//                stringBuilder.append(columnSchema.getColumnName()).append(",");
//            });
//
//            stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
//            stringBuilder.append(") VALUES (");
//
//
//        });

        for (TableSchema tableSchema : tableSchemaSet) {
            StringBuilder stringBuilder = tableSchemaStringBuilderMap.get(tableSchema);
//            tableSchema.getColumns().forEach(columnSchema -> {
//                stringBuilder.append(columnSchema.getColumnName()).append(",");
//            });
            for (ColumnSchema columnSchema : tableSchema.getColumns()) {
                System.out.println("column name "+columnSchema.getColumnName());
                System.out.println("id w insert  "+tableSchema.getId().getColumnName());
                System.out.println("id generowane?  "+columnSchema.isGeneratedId()+"\n");
                if (columnSchema.getColumnName().equals(tableSchema.getId().getColumnName()) && columnSchema.isGeneratedId() ) {
                    continue;
                }
                stringBuilder.append(columnSchema.getColumnName()).append(",");

            }

            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            stringBuilder.append(") VALUES (");


        }


        return this;
    }

    @Override
    QueryBuilder setValues() throws InvocationTargetException, IllegalAccessException {

        for (TableSchema tableSchema : tableSchemaSet) {
            StringBuilder stringBuilder = tableSchemaStringBuilderMap.get(tableSchema);

            for (ColumnSchema columnSchema : tableSchema.getColumns()) {

                if (columnSchema.getColumnName().equals(tableSchema.getId().getColumnName()) && columnSchema.isGeneratedId() ) {
                    continue;
                }

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

            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
            stringBuilder.append(");");

        }

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
        return this;
    }

    @Override
    QueryBuilder compose() {

        tableSchemaSet.forEach(tableSchema -> query.append(tableSchemaStringBuilderMap.get(tableSchema).toString()
        ).append(" "));

        return  this;
    }
}
