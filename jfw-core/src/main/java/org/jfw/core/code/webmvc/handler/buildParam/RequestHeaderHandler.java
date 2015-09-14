package org.jfw.core.code.webmvc.handler.buildParam;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.jfw.core.code.generator.annotations.webmvc.FieldParam;
import org.jfw.core.code.generator.annotations.webmvc.RequestHeader;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.handler.BuildParamHandler;

public class RequestHeaderHandler extends BuildParamHandler.BuildParameter{
    public final static RequestHeaderTransfer defaultRPT = new DefaultTransfer();

    @Override
    public void build(StringBuilder sb, int index, Type type, ControllerMethodCodeGenerator cmcg, Object annotation) {
        RequestHeader rp =(RequestHeader)annotation;
        if(type instanceof Class){
            buildClass( sb,  index, (Class<?>) type,  cmcg,  rp) ; 
        }else{
            RequestHeaderTransfer rpt=null;
            Class<RequestHeaderTransfer> cls = null;
            try {
                cls =HeaderTransferFactory.getRequestHeaderTransfer(type);
                if(cls!=null) rpt = cls.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("无法实例化的类型["+cls.toString()+"]");
            } 
            if(null== rpt) throw new RuntimeException("RequestHeaderHandler无法处理的类型["+type+"]");
            rpt.transfer(sb, index,type, cmcg, rp);
        }
    }
    public void buildClass(StringBuilder sb, int index, Class<?> clazz, ControllerMethodCodeGenerator cmcg, RequestHeader rp) {
        RequestHeaderTransfer rpt=null;
        try {
            Class<RequestHeaderTransfer> cls =HeaderTransferFactory.getRequestHeaderTransfer(clazz);
            if(cls!=null) rpt = cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("无法实例化的类型["+clazz.getName()+"]");
        } 
        if(null== rpt) rpt = new DefaultTransfer();
        rpt.transfer(sb, index,clazz, cmcg, rp); 
        
    }
    private static  class DefaultTransfer implements RequestHeaderTransfer{
        private RequestHeaderTransfer.FieldRequestParam frp[];
        private RequestHeader annotation;
        private Class<?> targetClass;
        
        private Type fieldType = null;
        
        private void initTargetType(Type type)
        {
           if(this.annotation.clazz()!=Object.class){
               this.targetClass = this.annotation.clazz();
           } else{
               this.targetClass = (Class<?>) type; 
           }
        }
        
        private void buildField()
        {
            List<RequestHeaderTransfer.FieldRequestParam> list = new LinkedList<RequestHeaderTransfer.FieldRequestParam>();
            for(Method method : this.targetClass.getMethods()){
                String mName = method.getName().trim();
                Type[] types = method.getGenericParameterTypes();
                if(mName.startsWith("set")&&(mName.length()>3)&& (types.length==1)&&(method.getReturnType()==void.class))
                {
                    Class<RequestHeaderTransfer> cls =HeaderTransferFactory.getRequestHeaderTransfer(types[0]);
                    if(null == cls) continue;
                    String fieldName = mName.substring(3);
                    if(fieldName.length()==1){
                        fieldName =fieldName.toUpperCase(Locale.ENGLISH);
                    }else{
                       fieldName=fieldName.substring(0,1).toUpperCase(Locale.ENGLISH)+fieldName.substring(1) ;
                    }
                    if(this.annotation.excludeFields().length>0){
                        boolean isContinue = false;
                        for(String ef:this.annotation.excludeFields()){
                            if(fieldName.equals(ef)){
                                isContinue= true;
                                continue;
                            }
                        }
                        if(isContinue) continue;
                    }
                    
                    RequestHeaderTransfer.FieldRequestParam rptFrp = new RequestHeaderTransfer.FieldRequestParam();
                    rptFrp.setClazz(Object.class);
                    rptFrp.setDefaultValue("");
                    rptFrp.setRequired(false);
                    if(this.annotation.value().trim().length()==0){
                    rptFrp.setParamName(fieldName);
                    }else{
                        rptFrp.setParamName(this.annotation.value().trim()+"."+fieldName);    
                    }
                    rptFrp.setValue(fieldName);
                    list.add(rptFrp);                    
                }
            }
            this.frp= list.toArray(new RequestHeaderTransfer.FieldRequestParam[list.size()]);
        }
        private void init()
        {
            if(this.annotation.fields().length!=0){
                frp = new RequestHeaderTransfer.FieldRequestParam[this.annotation.fields().length];
                for(int i = 0 ; i < this.annotation.fields().length ; ++i){
                    FieldParam fp = this.annotation.fields()[i];
                    frp[i]= new RequestHeaderTransfer.FieldRequestParam();
                    frp[i].setClazz(fp.clazz());
                    frp[i].setDefaultValue(fp.defaultValue());
                    frp[i].setRequired(fp.required());
                    frp[i].setValue(fp.value());
                    if(this.annotation.value().trim().length()==0){
                        frp[i].setParamName(fp.paramName().trim());
                    }else{
                        frp[i].setParamName(this.annotation.value().trim()+"."+fp.paramName().trim());
                    }
                }
            }else{
                buildField();
            }
        }
        private RequestHeaderTransfer getTransfer(RequestHeaderTransfer.FieldRequestParam rptFrp){
            Class<RequestHeaderTransfer> cls = null;
            for(Method method : this.targetClass.getMethods()){
                String mName = method.getName().trim();
                Type[] types = method.getGenericParameterTypes();
                if(mName.startsWith("set")&&(mName.length()>3)&& (types.length==1)&&(method.getReturnType()==void.class))
                {
                    String fieldName = mName.substring(3);
                    if(fieldName.length()==1){
                        fieldName =fieldName.toUpperCase(Locale.ENGLISH);
                    }else{
                       fieldName=fieldName.substring(0,1).toUpperCase(Locale.ENGLISH)+fieldName.substring(1) ;
                    }
                    if(fieldName.equals(rptFrp.getValue())){
                        cls =HeaderTransferFactory.getRequestHeaderTransfer(types[0]); 
                        this.fieldType = types[0];
                        if(cls!=null) break;
                    }
                }
            }
            if(cls!=null);
            try {
                return cls.newInstance();
            }  catch (Exception e) {
                throw new RuntimeException("无法实例化的类型["+cls.getName()+"]");
            }
        }
        
        @Override
        public void transfer(StringBuilder sb, int paramIndex,Type type, ControllerMethodCodeGenerator cmcg,
                RequestHeader annotation) {
            this.annotation = annotation;
            this.initTargetType(type);
            this.init();  
            sb.append("param").append(paramIndex).append(" = new ").append(this.targetClass.getName()).append("();");
            for(RequestHeaderTransfer.FieldRequestParam rptFrp:this.frp){
                RequestHeaderTransfer rptf = this.getTransfer(rptFrp);
                if(null==rptf) throw new RuntimeException("无法处理的字段["+this.targetClass.getName()+"."+rptFrp.getValue()+"]");
                rptf.transferBeanProperty(sb, paramIndex, this.fieldType, cmcg, rptFrp);
            }        
        }

        @Override
        public void transferBeanProperty(StringBuilder sb, int paramIndex, Type type,
                ControllerMethodCodeGenerator cmcg, RequestHeaderTransfer.FieldRequestParam frp) {
            throw new UnsupportedOperationException();            
        }
        
    }
}

