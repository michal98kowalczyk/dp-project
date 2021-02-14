package dp.orm.annotations;

public @interface JoinColumn {
    String name() default "";
    String referencedColumnName();
}
