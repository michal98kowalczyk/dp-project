package dp.orm.annotations;

import dp.orm.mapping.InheritanceMappingType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DatabaseTable {

    String name() default "";

    InheritanceMappingType inheritanceType() default InheritanceMappingType.SINGLE_TABLE;

}
