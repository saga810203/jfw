package org.jfw.core.code.webmvc.handler.buildParam;

import org.jfw.core.code.utils.Utils;

public  class BaseTypeRPT extends AbstractRequestParamTransfer {

    public void transferToParam(){
        Class<?> cls=(Class<?>)this.type;
        if(cls==int.class){
            this.sb.append("Integer.parseInt(param)");
        } else if(cls==Integer.class){
            this.sb.append("Integer.valueof(param)");
        } else if(cls==byte.class){
            this.sb.append("Byte.parseByte(param)");
        } else if(cls==Byte.class){
            this.sb.append("Byte.valueof(param)");
        } else if(cls==short.class){
            this.sb.append("Short.parseShort(param)");
        } else if(cls==Short.class){
            this.sb.append("Short.valueof(param)");
        } else if(cls==long.class){
            this.sb.append("Long.parseLong(param)");
        } else if(cls==Long.class){
            this.sb.append("Long.valueof(param)");
        } else  if(cls==double.class){
            this.sb.append("Double.parseDouble(param)");
        } else if(cls==Double.class){
            this.sb.append("Double.valueof(param)");
        } else if(cls==float.class){
            this.sb.append("Float.parseFloat(param)");
        } else if(cls==Float.class){
            this.sb.append("Float.valueof(param)");
        } else if(cls==boolean.class || cls==Boolean.class){
            this.sb.append( "\"1\".equals(param)|| \"true\".equalsIgnoreCase(param)||\"yes\".equalsIgnoreCase(param)");
        } else if(cls==String.class){
            this.sb.append("param");
        } else if(cls==java.math.BigInteger.class){
            this.sb.append("new  java.math.BigInteger(param)");
        } else if(cls==java.math.BigDecimal.class){
            this.sb.append("new java.math.BigDecimal(param)");
        } else{
            throw new IllegalArgumentException("不支持的类型："+cls.getName());
        }
       
    }

    @Override
    public void bulidParam() {
        this.cmcg.readParameter(this.annotation.value().trim());
        if (this.annotation.required()){
            sb.append("if(null==param || param.length()==0){");
            this.raiseNoFoundError(this.annotation.value().trim());
            sb.append("}\r\n");
            Utils.writeNameOfType(this.type, sb);
            sb.append(" param").append(this.paramIndex).append(" = ");
            this.transferToParam();
            sb.append(";\r\n");
        }else{
            Utils.writeNameOfType(this.type, sb); 
            sb.append(" param").append(this.paramIndex);
            Class<?> cls = (Class<?>) this.type;
            if(cls.isPrimitive()){
                if(this.annotation.defaultValue().trim().length()!=0 && !"null".equals(this.annotation.defaultValue())){
                   sb.append(" = ").append(this.annotation.defaultValue()); 
                }
            }else{
                   sb.append(" = ").append(this.annotation.defaultValue().trim().length()==0?"null":this.annotation.defaultValue()); 
                
            }
            sb.append(";\r\n");
            sb.append("if(null!=param && param.length()!=0){\r\n");
            sb.append(" param").append(this.paramIndex).append(" = ");
            this.transferToParam();
            sb.append(";\r\n");
            sb.append("}\r\n");           
        }
    }

    @Override
    public void bulidBeanProterty() {
        this.checkRequestFieldParamName();
        this.cmcg.readParameter(this.frp.getParamName().trim());
        if (this.frp.isRequired()){
            sb.append("if(null==param || param.length()==0){");
            this.raiseNoFoundError(this.frp.getParamName().trim());
            sb.append("}\r\n");
            sb.append(" param").append(this.paramIndex).append("set").append(Utils.getFieldNameInMethod(this.frp.getValue().trim())).append("(");
            this.transferToParam();
            sb.append(");\r\n");
        }else{
            sb.append("if(null!=param && param.length()!=0){\r\n");
            sb.append(" param").append(this.paramIndex).append(".set").append(Utils.getFieldNameInMethod(this.frp.getValue().trim()))
               .append("(");
            this.transferToParam();
            sb.append(");\r\n");
            sb.append("}\r\n");  
            Class<?> cls =(Class<?>)this.type;
            if(cls.isPrimitive()){
                if(this.frp.getDefaultValue().trim().length()!=0 && !"null".equals(this.frp.getDefaultValue())){
                   sb.append("else\r\n{ \r\n    param").append(this.paramIndex).append("set").append(Utils.getFieldNameInMethod(this.frp.getValue().trim()))
                   .append("(")
            .append(this.frp.getDefaultValue()).append(");\r\n}\r\n");
                }
            }else{
                if(this.frp.getDefaultValue().trim().length()!=0){
                    sb.append("else\r\n{ \r\n    param").append(this.paramIndex).append("set").append(Utils.getFieldNameInMethod(this.frp.getValue().trim()))
                    .append("(")
             .append(this.frp.getDefaultValue()).append(");\r\n}\r\n");
                }
            } 
        }    
    }

}
