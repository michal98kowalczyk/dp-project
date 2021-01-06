package dp.orm.utlis;

import com.google.common.base.CaseFormat;
import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;

import java.lang.reflect.Field;

public class NameUtils {

    public static String extractTableName(Class<?> clazz) {
        DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
        String name = null;
        if ( databaseTable != null && !databaseTable.name()
                .isEmpty() ) {
            name = databaseTable.name();
        }
        if ( name == null ) {
            name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName());
        }

        return name;
    }

    public static String extractColumnName(Field field) {
        DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
        String name = null;
        if ( databaseField != null && !databaseField.name()
                .isEmpty() ) {
            name = databaseField.name();
        }
        if ( name == null ) {
            name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }

        return name;
    }
}
