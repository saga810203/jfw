package org.jfw.core.code.webmvc.handler.view;

import org.jfw.core.code.generator.annotations.webmvc.StringJSP;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.handler.ViewHandler;

public class StringJspView extends ViewHandler.BuildView {

    @Override
    public void before(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
    	if(cmcg.getMethod().getReturnType()!=String.class) throw new RuntimeException("@StringJSP 只能修修饰 返回类型为String 的方法");
        sb.append("try{");
    }
    @Override
    public void after(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
    	StringJSP jsp =(StringJSP)annotation;
        sb.append("}catch(Exception e){");
        //FIXME: handler error
        sb.append("\r\n//TODO:impl application logic handler  in org.jfw.core.code.webmvc.handler.view.JspView  \r\n res.sendError(500);\r\nreturn;\r\n}");  
        if(cmcg.getMethod().getGenericReturnType()!=void.class){
            sb.append("req.setAttribute(\"jfw.web.request.data\",result);\r\n");
        }
        sb.append("req.getRequestDispatcher(\"").append(jsp.prefix().trim()).append("\"+result+\"").append(".jsp\").forward(req,res);");  
    }

}

