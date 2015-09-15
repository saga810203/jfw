package org.jfw.core.code.generator.annotations.webmvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.webmvc.handler.BuildParamHandler.BuildParameter;
import org.jfw.core.code.webmvc.handler.buildParam.RequestHeaderHandler;
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestHeader {
    String value() default "";
    Class<?> clazz() default Object.class;
    String defaultValue() default "null";
    boolean required() default true;
    FieldParam[] fields() default {};
    String[] excludeFields() default {};
    Class<? extends BuildParameter> buildParamClass() default RequestHeaderHandler.class ;
}
