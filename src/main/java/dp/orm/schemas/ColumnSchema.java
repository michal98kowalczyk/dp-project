package dp.orm.schemas;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ColumnSchema {
    private Class<?> parent;

    private Method getter;

    private Method setter;

    private String columnName;

    private String sqlType;

    private Class javaType;

    private boolean isGeneratedId;

    private boolean isNullable;

    private boolean isForeignKey;

}
