package org.jfw.core.code.generator.annotations.webmvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldParam {
    String value() ;
    String paramName() default "";
    Class<?> clazz() default Object.class;
    String defaultValue() default "null";
    boolean required() default true;
}
