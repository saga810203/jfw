package org.jfw.core.code.generator.annotations.webmvc;

import org.jfw.core.code.webmvc.handler.BuildParamHandler.BuildParameter;
import org.jfw.core.code.webmvc.handler.buildParam.SessionAttributeHandler;

public @interface SessionAttribute {
    String value();
    boolean required() default true;
    Class<? extends BuildParameter> buildParamClass() default SessionAttributeHandler.class ;
}
