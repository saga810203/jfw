package org.jfw.core.code.generator.orm.impl;

public class BooleanSet extends AbstractSetHandler {

    protected  String getMethodName4JDBCWrite(){
        return "setString";
    }
    protected  int getJdbcType()
    {
        return java.sql.Types.CHAR;
    }
  public void init(String beanName, String fieldName, String el4ReadValue, Class<?> javaType, boolean nullable) {
        this.javaType = javaType;
        this.nullable = nullable;
        if (this.javaType.isPrimitive())
            this.nullable = false;
        if (el4ReadValue != null && el4ReadValue.trim().length() > 0) {
            this.el4Read = el4ReadValue.trim();
        } else if (fieldName != null && (fieldName.trim().length() > 0)) {
            if(this.javaType==boolean.class){
                this.el4Read = beanName.trim() + ".is" + Utils.getFieldNameInMethod(fieldName) + "()";
            }else{
                this.el4Read = beanName.trim() + "." + Utils.getGetter(fieldName) + "()";}
        } else {
            this.el4Read = beanName.trim();
        }

    }

    protected void checkLocal(StringBuilder sb) {
        if (null == this.local) {
            local = Utils.getLocalVarName();
            sb.append(this.javaType.getName()).append(" ").append(this.local).append("=").append(this.el4Read)
                    .append(";");
        }
    }

    public void wirteValue(StringBuilder sb) {
        if (this.javaType.isPrimitive()) {
            this.wirteNotNullValue(sb);
            return;
        }
        this.checkLocal(sb);
        sb.append("if(null!=").append(this.local).append("){").append("  ps.").append(this.getMethodName4JDBCWrite())
                .append("(paramIndex++,").append(this.local).append( "?\"1\":\"0\");").append("}else{")
                .append("  ps.setNull(paramIndex++,").append(this.getJdbcType()).append(");").append("}");
    }

    public void wirteNotNullValue(StringBuilder sb) {
        if (null == local) {
            sb.append("ps." + this.getMethodName4JDBCWrite() + "(paramIndex++," + this.el4Read + "?\"1\":\"0\");");
        } else {
            sb.append("ps." + this.getMethodName4JDBCWrite() + "(paramIndex++," + local + "?\"1\":\"0\");");
        }
    }

    public void wirteValueWithCheck(StringBuilder sb) {
        if (this.javaType.isPrimitive())
            throw new RuntimeException("don't call this method with primitive class");
        this.checkLocal(sb);
        sb.append("if(null!=").append(this.local).append("){").append("  ps.").append(this.getMethodName4JDBCWrite())
                .append("(paramIndex++,").append(this.local).append( "?\"1\":\"0\");").append("}");
    }

    public void codeBeginCheckInSetOrWhere(StringBuilder sb) {
        if (this.javaType.isPrimitive())
            throw new RuntimeException("don't call this method with primitive class");
        this.checkLocal(sb);
        sb.append("if(null!=").append(this.local).append("){");
    }

    public void codeELESCheckInSetOrWhere(StringBuilder sb) {
        if (this.javaType.isPrimitive())
            throw new RuntimeException("don't call this method with primitive class");
        sb.append("}else{");
    }

    public void codeEndCheckInSetOrWhere(StringBuilder sb) {
        if (this.javaType.isPrimitive())
            throw new RuntimeException("don't call this method with primitive class");
        sb.append("}");
    }

}
