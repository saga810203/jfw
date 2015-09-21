package org.jfw.core.code.webmvc.handler.buildParam;

import org.jfw.core.code.utils.Utils;

public class BaseArrayRPT extends AbstractRequestParamTransfer {
    private Class<?> baseClass = null;

    public void transferToParams() {
        Class<?> cls = (Class<?>) this.baseClass;

        if (cls == int.class) {
            this.sb.append("Integer.parseInt(params[i])");
        } else if (cls == Integer.class) {
            this.sb.append("Integer.valueof(params[i])");
        } else if (cls == byte.class) {
            this.sb.append("Byte.parseByte(params[i])");
        } else if (cls == Byte.class) {
            this.sb.append("Byte.valueof(params[i])");
        } else if (cls == short.class) {
            this.sb.append("Short.parseShort(params[i])");
        } else if (cls == Short.class) {
            this.sb.append("Short.valueof(params[i])");
        } else if (cls == long.class) {
            this.sb.append("Long.parseLong(params[i])");
        } else if (cls == Long.class) {
            this.sb.append("Long.valueof(params[i])");
        } else if (cls == double.class) {
            this.sb.append("Double.parseDouble(params[i])");
        } else if (cls == Double.class) {
            this.sb.append("Double.valueof(params[i])");
        } else if (cls == float.class) {
            this.sb.append("Float.parseFloat(params[i])");
        } else if (cls == Float.class) {
            this.sb.append("Float.valueof(params[i])");
        } else if (cls == boolean.class || cls == Boolean.class) {
            this.sb.append("\"1\".equals(params[i])|| \"true\".equalsIgnoreCase(params[i])||\"yes\".equalsIgnoreCase(params[i])");
        } else if (cls == String.class) {
            this.sb.append("params[i]");
        } else if (cls == java.math.BigInteger.class) {
            this.sb.append("new java.math.BigInteger(params[i])");
        } else if (cls == java.math.BigDecimal.class) {
            this.sb.append("new java.math.BigDecimal(params[i])");
        } else {
            throw new IllegalArgumentException("不支持的类型：" + cls.getName());
        }
    }

    @Override
    public void bulidParam() {
        baseClass = ((Class<?>) this.type).getComponentType();
        this.cmcg.readParameters(this.annotation.value().trim());
        if (this.annotation.required()) {
            sb.append("if(null==params || params.length==0){");
            this.raiseNoFoundError(this.annotation.value().trim());
            sb.append("}\r\n").append(this.baseClass.getName()).append("[] param").append(this.paramIndex)
                    .append(" = new ").append(this.baseClass.getName()).append("[params.length];\r\n");
            sb.append("for( int i = 0 ; i < params.length ; ++i){\r\n").append("  param").append(this.paramIndex)
                    .append("[i]=");
            this.transferToParams();
            sb.append(";\r\n}\r\n");
        } else {
            sb.append(this.baseClass.getName()).append("[] param").append(this.paramIndex).append(" ;\r\n ");
            sb.append("if(null!=params && params.length!=0){");
            sb.append("for( int i = 0 ; i < params.length ; ++i){\r\n").append("  param").append(this.paramIndex)
                    .append("[i]=");
            this.transferToParams();
            sb.append(";\r\n}\r\n");
            sb.append("}else{\r\n");
            sb.append(" param")
                    .append(this.paramIndex)
                    .append(" = ")
                    .append(this.annotation.defaultValue().trim().length() == 0 ? "null" : this.annotation
                            .defaultValue());
            sb.append(";\r\n}\r\n");
        }
    }

    @Override
    public void bulidBeanProterty() {
        baseClass = ((Class<?>) this.type).getComponentType();
        String localName = Utils.getLocalVarName();
        this.checkRequestFieldParamName();
        this.cmcg.readParameter(this.frp.getParamName().trim());
        if (this.frp.isRequired()) {
            sb.append("if(null==params || params.length==0){");
            this.raiseNoFoundError(this.frp.getParamName().trim());
            sb.append("}\r\n");
            sb.append(this.baseClass.getName()).append("[] ").append(localName).append(" = new ")
                    .append(this.baseClass.getName()).append("[params.length];");
            sb.append("for( int i = 0 ; i < params.length ; ++i){\r\n").append(localName).append("[i]=");
            this.transferToParams();
            sb.append(";\r\n}\r\n");
            sb.append(" param").append(this.paramIndex).append("set")
                    .append(Utils.getFieldNameInMethod(this.frp.getValue().trim())).append("(");
            sb.append(localName);
            sb.append(");\r\n");
        } else {
            sb.append("if(null!=params && params.length!=0){\r\n");
            sb.append(this.baseClass.getName()).append("[] ").append(localName).append(" = new ")
                    .append(this.baseClass.getName()).append("[params.length];");
            sb.append("for( int i = 0 ; i < params.length ; ++i){\r\n").append(localName).append("[i]=");
            this.transferToParams();
            sb.append(";\r\n}\r\n");
            sb.append(" param").append(this.paramIndex).append("set")
                    .append(Utils.getFieldNameInMethod(this.frp.getValue().trim())).append("(");
            sb.append(localName);
            sb.append(");\r\n");
            sb.append("\r\n}\r\n");
            if (this.frp.getDefaultValue().trim().length() != 0) {
                sb.append("else{");
                sb.append(" param").append(this.paramIndex).append("set")
                        .append(Utils.getFieldNameInMethod(this.frp.getValue().trim())).append("(");
                sb.append(this.frp.getDefaultValue());
                sb.append(");\r\n");
                sb.append("\r\n}\r\n");
            }
        }
    }
}
