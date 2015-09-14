package org.jfw.core.code.generator.annotations.webmvc;

import org.jfw.core.code.webmvc.handler.BuildParamHandler.BuildParameter;
import org.jfw.core.code.webmvc.handler.buildParam.RequestBodyHandler;

public @interface RequestBody {
    Class<? extends BuildParameter> buildParamClass() default RequestBodyHandler.class ;
}
