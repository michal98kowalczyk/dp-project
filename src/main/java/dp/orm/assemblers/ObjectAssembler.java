package dp.orm.assemblers;

import dp.orm.utlis.FieldUtils;
import dp.orm.utlis.NameUtils;
import org.aopalliance.intercept.Invocation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ObjectAssembler<T> {

    public T assemble(Class<T> clazz, ResultSet resultSet) throws NoSuchMethodException, NoSuchElementException, IllegalAccessException,
            InvocationTargetException, InstantiationException, SQLException {
        T object = clazz.getConstructor().newInstance();
        getObjectFromResultSet(resultSet, object);
        return object;
    }

    public List<T> bulkAssemble(Class<T> clazz, ResultSet resultSet) throws SQLException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> resultList = new ArrayList<>();
        while (resultSet.next()) {
            resultList.add(assemble(clazz, resultSet));
        }
        return resultList;
    }

    private void getObjectFromResultSet(ResultSet resultSet, Object object) throws IllegalArgumentException,
            IllegalAccessException, SQLException {
        Class<?> clazz = object.getClass();
        for (Field field : FieldUtils.getAllFields(clazz)) {
            field.setAccessible(true);
            Object value = resultSet.getObject(NameUtils.extractColumnName(field));

            if (value != null) {
                Class<?> type = field.getType();
                Class<?> boxed = boxPrimitiveClass(type);
                boxed.cast(value);
                field.set(object, value);
            }
        }
    }

    private Class<?> boxPrimitiveClass(Class<?> type) {
        if (type == int.class) {
            return Integer.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == double.class) {
            return Double.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == char.class) {
            return Character.class;
        } else if (type == short.class) {
            return Short.class;
        } else {
            return type;
        }
    }


}
