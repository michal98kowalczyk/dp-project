package dp.orm.utlis;

import dp.orm.annotations.DatabaseField;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FieldUtils {


    public static <T> List<Field> getAllFields(Class<T> cls){

        List<Field> fields = new ArrayList<>();

        Field[] declaredFieldsInClass = cls.getDeclaredFields();
        fields.addAll(Arrays.asList(declaredFieldsInClass));

        Class<? super T> superClass = cls.getSuperclass();

        while (superClass != null && !superClass.equals(Object.class)){
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));

            superClass = superClass.getSuperclass();
        }


//        return only fields which are fields of database, so check if our annotation is in class
        return fields.stream().filter(field -> field.isAnnotationPresent(DatabaseField.class)
        && !field.getName().startsWith("this")).collect(Collectors.toList());
    }




}
