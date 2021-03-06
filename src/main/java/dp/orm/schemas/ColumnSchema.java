package dp.orm.schemas;

import dp.orm.ForeignKey.ForeignKeyEntity;
import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.Id;
import dp.orm.exceptions.IncorrectIdException;
import dp.orm.query.Java2SqlTypeMapper;
import dp.orm.utlis.NameUtils;
import lombok.*;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
public class ColumnSchema {
    private Class<?> parent;

    private Method getter;

    private Method setter;

    private String columnName;

    private String sqlType;

    private Class javaType;

    private boolean isGeneratedId;

    private boolean isNullable;

    private boolean isForeignKey = false;

    private ForeignKeyEntity foreignKey = null;

    public ColumnSchema(Field field){

        this.columnName = NameUtils.extractColumnName(field);

        //sprawdzamy czy jest oznaczenie id
        if (field.isAnnotationPresent(Id.class)){
            //id powinno byc intem
            if (field.getType() != int.class) throw new IncorrectIdException("Wrong Id Type") ;

            this.isGeneratedId = field.getAnnotation(Id.class).
                    generated();
            System.out.println("czy id bedzie generowane" +field.getAnnotation(Id.class).
                    generated());

        }else{
            this.isGeneratedId = false;

        }
        this.parent = field.getDeclaringClass();
        this.javaType = field.getType();
        this.sqlType = Java2SqlTypeMapper.getSqlType(field.getType());


        this.isNullable = field.getAnnotation(DatabaseField.class).nullable();

        this.getter = findGetter(field);
        this.setter = findSetter(field);
        }

    private Method findSetter(Field field) {
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();

        Method _setter = null;

        try {
                _setter = this.parent.getDeclaredMethod("set" + StringUtils.capitalize(fieldName), fieldType);
        }catch (NoSuchMethodException ee){
            ee.printStackTrace();
        }

        if ( _setter == null|| _setter.getReturnType() != fieldType ) return null;

        return _setter;
    }

    private Method findGetter(Field field) {
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();

        Method _getter = null;

        try {
            if (fieldType.equals(boolean.class)){
                _getter = this.parent.getDeclaredMethod("is"+StringUtils.capitalize(fieldName));
            }else {
                _getter = this.parent.getDeclaredMethod("get"+StringUtils.capitalize(fieldName));
            }
        }catch (NoSuchMethodException ee){
            ee.printStackTrace();
        }

        //sprawdzamy najpierw czy nie null zeby nie wywolac przypadkiem null.getReturnType -> analogia jak w setterze
        if (_getter == null || _getter.getReturnType() != fieldType) return null;

        return _getter;
    }

    public Object get(Object obj){
        try {
            return this.getter.invoke(obj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Error! Getting value is impossible!");
        }

    }

    public void set(Object obj,Object value){
        try {
            this.setter.invoke(obj,value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Error! Setting value is impossible!");
        }
    }

    public void setForeignKey(ForeignKeyEntity foreignKey) {
        this.foreignKey = foreignKey;
        this.isForeignKey = true;
    }

    public boolean isForeignKey() {
        return isForeignKey;
    }

    public ForeignKeyEntity getForeignKey() {
        return this.foreignKey;
    }
}
