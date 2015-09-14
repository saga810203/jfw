package org.jfw.core.code.webmvc.handler.buildParam;

import java.lang.reflect.Type;

import org.jfw.core.code.utils.Utils;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.handler.BuildParamHandler;

public class RequestBodyHandler extends BuildParamHandler.BuildParameter{

    @Override
    public void build(StringBuilder sb, int index, Type type, ControllerMethodCodeGenerator cmcg, Object annotation) {
        Utils.writeNameOfType(type, sb);
        sb.append(" param").append(index).append(";");        
        String localName = Utils.getLocalVarName();
        sb.append(localName).append(" = req.getInputStream();\r\n")
           .append("try{\r\n").append("param").append(index).append(" = ");
        if(type instanceof Class){
            sb.append("org.jfw.util.json.JsonService.fromJson(new java.io.InputStreamReader(").append(localName)
               .append(", org.jfw.util.ConstData.UTF8),");
            Utils.writeNameOfType(type, sb);
            sb.append(".class);");
        }else{
            sb.append("org.jfw.util.json.JsonService.<");
            Utils.writeNameOfType(type, sb);
            sb.append(">fromJson(new java.io.InputStreamReader(").append(localName)
            .append(", org.jfw.util.ConstData.UTF8),new new org.jfw.util.reflect.TypeReference<");
         Utils.writeNameOfType(type, sb);
         sb.append(">(){}.getType() );");  
        }
        sb.append("}finally{\r\n").append(localName).append(".close();}");
    }

}
