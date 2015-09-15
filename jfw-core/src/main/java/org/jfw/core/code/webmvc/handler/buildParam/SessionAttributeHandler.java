package org.jfw.core.code.webmvc.handler.buildParam;

import java.lang.reflect.Type;

import org.jfw.core.code.generator.annotations.webmvc.SessionAttribute;
import org.jfw.core.code.utils.Utils;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.handler.BuildParamHandler;

public class SessionAttributeHandler extends BuildParamHandler.BuildParameter{

    @Override
    public void build(StringBuilder sb, int index, Type type, ControllerMethodCodeGenerator cmcg, Object annotation) {
       if(type instanceof Class<?>&&(((Class<?>)type).isPrimitive())){
    	   throw new RuntimeException("annotion @SessionAttribute don't decorate primitive type");
       }
    	
    	SessionAttribute sa =(SessionAttribute)annotation;
        cmcg.readSession();
        if(sa.required()){
            String localName = Utils.getLocalVarName();
            sb.append("Object ").append(localName).append(" = session.getAttribute(\"").append(sa.value().trim()).append("\");\r\n")
               .append("if(null==").append(localName).append("){")
               .append("throw new IllegalArgumentException(\"没有Session值："+sa.value().trim()+"\");")
               .append("}");
            Utils.writeNameOfType(type, sb);
             sb.append(" param").append(index).append(" = (");
             Utils.writeNameOfType(type, sb);
             sb.append(")").append(localName).append(";");           
        }else{
        	 Utils.writeNameOfType(type, sb);
             sb.append(" param").append(index).append(" = session.getAttribute(\"").append(sa.value().trim()).append("\");\r\n");   
        }
    }

}
