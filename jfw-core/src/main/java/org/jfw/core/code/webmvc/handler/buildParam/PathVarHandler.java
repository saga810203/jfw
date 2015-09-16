package org.jfw.core.code.webmvc.handler.buildParam;

import java.lang.reflect.Type;

import org.jfw.core.code.generator.annotations.webmvc.PathVar;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.handler.BuildParamHandler;

public class PathVarHandler extends BuildParamHandler.BuildParameter{

    @Override
    public void build(StringBuilder sb, int index, Type type, ControllerMethodCodeGenerator cmcg, Object annotation) {
      PathVar pv = (PathVar)annotation;        
        cmcg.readURI(pv.skipChars());
        
    }

}
