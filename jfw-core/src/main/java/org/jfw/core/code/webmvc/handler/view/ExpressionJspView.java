package org.jfw.core.code.webmvc.handler.view;

import org.jfw.core.code.generator.annotations.webmvc.ExpressionJSP;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.handler.ViewHandler;
public class ExpressionJspView  extends ViewHandler.BuildView {
	private ExpressionJSP ej = null;

    @Override
    public void before(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
    	ej =(ExpressionJSP)annotation;
    	if(ej.condition().length==0) throw new RuntimeException("must set attribute condition in @ExpressionJSP");
    	sb.append("try{");
    }
    @Override
    public void after(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
        sb.append("}catch(Exception e){");
        //FIXME: handler error
        sb.append("\r\n//TODO:impl application logic handler  in org.jfw.core.code.webmvc.handler.view.JspView  \r\n res.sendError(500);\r\nreturn;\r\n}");  
        if(cmcg.getMethod().getGenericReturnType()!=void.class){
            sb.append("req.setAttribute(\"jfw.web.request.data\",result);\r\n");
        }
        for(int i = 0 ; i < ej.condition().length ; ++i){
        	if(0!=i) sb.append("else ");
        	sb.append("  if(").append(ej.condition()[i].el()).append("){ \r\nreq.getRequestDispatcher(\"").append(ej.condition()[i].prefix().trim()).append(ej.condition()[i].jsp().trim()).append(".jsp\").forward(req,res);\r\n}\r\n");  
        }
        sb.append("else {\r\nreq.getRequestDispatcher(\"").append(ej.defaultPrefix().trim()).append(ej.defaultJsp().trim()).append(".jsp\").forward(req,res);");  
    }

}

