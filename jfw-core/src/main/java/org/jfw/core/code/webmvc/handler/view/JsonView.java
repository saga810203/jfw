package org.jfw.core.code.webmvc.handler.view;

import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.handler.ViewHandler;

public class JsonView extends ViewHandler.BuildView {

    @Override
    public void before(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
       cmcg.readOut();
        sb.append("try{");
    }

    @Override
    public void after(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
       
        sb.append("}catch(Exception e){");
        //FIXME: handler error
        sb.append("return;\r\n}");        
        if(cmcg.getMethod().getGenericReturnType()!=void.class){
            sb.append("out.write(\"{\\\"success\\\":true,\\\"data\\\":\");\r\n")
               .append("org.jfw.util.json.JsonService.toJson(result,out);\r\n")
               .append("out.write(\"}\");");             
        }else{
            sb.append("out.writer(\"{\\\"success\\\":true}\");");  
        }
    }

}
