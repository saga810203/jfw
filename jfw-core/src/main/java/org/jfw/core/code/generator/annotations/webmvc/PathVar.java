package org.jfw.core.code.generator.annotations.webmvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.webmvc.handler.BuildParamHandler.BuildParameter;
import org.jfw.core.code.webmvc.handler.buildParam.PathVarHandler;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PathVar {
    String value() ;
    boolean encoding() default false;
    String pathAttribute() default "org.jfw.web.reqMacthUri_DYN";
    Class<? extends BuildParameter> buildParamClass() default PathVarHandler.class ;
}
