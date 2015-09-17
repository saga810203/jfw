package org.jfw.core.code.webmvc.handler.view;

import org.jfw.core.code.generator.annotations.webmvc.JSP;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.handler.ViewHandler;

public class JspView extends ViewHandler.BuildView {

    @Override
    public void before(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
       sb.append("try{");
    }
    @Override
    public void after(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
    	JSP jsp =(JSP)annotation;
        sb.append("}catch(Exception e){");
        //FIXME: handler error
        sb.append("\r\n//TODO:impl application logic handler  in org.jfw.core.code.webmvc.handler.view.JspView  \r\n res.sendError(500);\r\nreturn;\r\n}");  
        if(cmcg.getMethod().getGenericReturnType()!=void.class){
            sb.append("req.setAttribute(\"jfw.web.request.data\",result);\r\n");
        }
        sb.append("req.getRequestDispatcher(\"").append(jsp.prefix().trim()).append(jsp.value().trim()).append(".jsp\").forward(req,res);");  
    }

}
