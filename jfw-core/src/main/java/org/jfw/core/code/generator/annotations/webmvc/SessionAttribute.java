package org.jfw.core.code.generator.annotations.webmvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.webmvc.handler.BuildParamHandler.BuildParameter;
import org.jfw.core.code.webmvc.handler.buildParam.SessionAttributeHandler;
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionAttribute {
    String value();
    boolean required() default true;
    Class<? extends BuildParameter> buildParamClass() default SessionAttributeHandler.class ;
}
