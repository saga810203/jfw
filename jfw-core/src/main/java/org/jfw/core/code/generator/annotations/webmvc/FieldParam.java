package org.jfw.core.code.generator.annotations.webmvc;

public @interface FieldParam {
    String value() ;
    String paramName() default "";
    Class<?> clazz() default Object.class;
    String defaultValue() default "null";
    boolean required() default true;
}
