package dp.orm.query;

import dp.orm.exceptions.NullableFieldException;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class InsertQuery extends QueryBuilder {

    private InheritanceMapping inheritanceMapping;
    Map<TableSchema,StringBuilder> tableSchemaStringBuilderMap;
    Set<TableSchema> tableSchemaSet;
    Object object;
    List<Field> fields;


    public InsertQuery(InheritanceMapping inheritanceMapping) {
        this.inheritanceMapping = inheritanceMapping;
        this.query = new StringBuilder();
        this.fields = new LinkedList<>();
        this.tableSchemaSet = new HashSet<>();
        this.tableSchemaStringBuilderMap = new HashMap<>();
    }

    @Override
    <T> QueryBuilder withObject(T object) {

        this.object = object;
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
