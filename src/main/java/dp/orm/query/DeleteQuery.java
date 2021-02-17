package dp.orm.query;

import dp.orm.Dao;
import dp.orm.OrmManager;
import dp.orm.annotations.ForeignKey;
import dp.orm.annotations.JoinColumn;
import dp.orm.annotations.OneToMany;
import dp.orm.annotations.OneToOne;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.TableSchema;
import dp.orm.utlis.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DeleteQuery extends QueryBuilder {

    private InheritanceMapping inheritanceMapping;
    Map<TableSchema,StringBuilder> tableSchemaStringBuilderMap;
    Set<TableSchema> tableSchemaSet;
    Object object;
    List<Field> fields;

    public DeleteQuery(InheritanceMapping inheritanceMapping){
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
        fields = FieldUtils.getAllFields((Class)object);

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
            tableSchemaStringBuilderMap.get(tableSchema).append("DELETE FROM ");
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
        return this;
    }

    @Override
    QueryBuilder setValues() throws InvocationTargetException, IllegalAccessException {
        return this;
    }

    @Override
    QueryBuilder withCondition(int id, boolean isConditionSet) {

        checkAnnotations(fields,id);

        Optional<Field> foreignKey = getforeignKeyIfExists(fields);


        if(foreignKey.isPresent()){
            deleteByForeignKey(foreignKey.get(),id);
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


    private void checkAnnotations(List<Field> fields,int id) {


        fields.forEach(field -> {


            if (  (field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(OneToMany.class)) && field.isAnnotationPresent(JoinColumn.class) ){


                Class referencedClass = field.getAnnotation(JoinColumn.class).referencedClass();

                createSubQuery(referencedClass,id);


            }


        });
    }

    private void deleteByForeignKey(Field field, int id) {

        String name = field.getAnnotation(ForeignKey.class).name();


        tableSchemaSet.forEach(tableSchema -> {
            tableSchemaStringBuilderMap.get(tableSchema).append(" WHERE ").append(name).append(" = ").append(id).append(";");
        });



    }

    private void createSubQuery(Class referencedClass, int id) {


        try {
            Dao<Object> dao = OrmManager.getDao(referencedClass);
            InheritanceMapping mapping = dao.getMapping();
            QueryBuilder queryBuilder = new DeleteQuery(mapping);
            QueryDirector<Object> queryDirector = new QueryDirector<>(queryBuilder);
            String queryTmp = queryDirector.withObject(referencedClass).withCondition(id).build();
            subQuery.append(queryTmp).append(" ");

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
