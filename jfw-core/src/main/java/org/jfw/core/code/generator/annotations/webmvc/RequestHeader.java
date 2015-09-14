package org.jfw.core.code.generator.annotations.webmvc;

import org.jfw.core.code.webmvc.handler.BuildParamHandler.BuildParameter;
import org.jfw.core.code.webmvc.handler.buildParam.RequestHeaderHandler;

public @interface RequestHeader {
    String value() default "";
    Class<?> clazz() default Object.class;
    String defaultValue() default "null";
    boolean required() default true;
    FieldParam[] fields() default {};
    String[] excludeFields() default {};
    Class<? extends BuildParameter> buildParamClass() default RequestHeaderHandler.class ;
}
