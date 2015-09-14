package org.jfw.core.code.webmvc.handler.buildParam;

import java.lang.reflect.Type;

import org.jfw.core.code.generator.annotations.webmvc.RequestHeader;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;

public abstract class AbstractRequestHeaderTransfer implements RequestHeaderTransfer {
    protected StringBuilder sb ;
    protected int paramIndex;
    protected Type type;
    protected ControllerMethodCodeGenerator cmcg;
    protected RequestHeader annotation;
    protected RequestHeaderTransfer.FieldRequestParam frp;
    
    public  abstract void bulidParam();
    public  abstract void bulidBeanProterty();
    public void checkRequestParamName()
    {
        if(annotation.value()==null || annotation.value().trim().length()==0)
        {
            throw new RuntimeException("@RequestParam 没有定义参数名");
        } 
    }
    public void checkRequestFieldParamName()
    {
        if(this.frp.getValue()==null || this.frp.getValue().trim().length()==0)
        {
            throw new RuntimeException("@RequestParam.fields 没有定义参数名");
        } 
    }
    public void raiseNoFoundError(String paramName){
        this.sb.append("throw new IllegalArgumentException(\"没有传递参数："+paramName+"\");");
    }
    

    @Override
    public void transfer(StringBuilder s, int p,Type t, ControllerMethodCodeGenerator c,RequestHeader annotation) {
        this.sb=s;
        this.paramIndex=p;
        this.type=t;
        this.cmcg=c;
        this.annotation=annotation;
        this.frp = null;
        this.bulidParam();
        
        
    }
  

    @Override
    public void transferBeanProperty(StringBuilder s, int p, Type t,
            ControllerMethodCodeGenerator c, RequestHeaderTransfer.FieldRequestParam frp) {
        this.sb=s;
        this.paramIndex=p;
        this.type=t;
        this.cmcg=c;
        this.frp = frp;
        this.bulidBeanProterty();
    }
     
}
