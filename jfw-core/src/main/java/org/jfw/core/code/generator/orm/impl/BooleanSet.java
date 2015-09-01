package org.jfw.core.code.generator.orm.impl;

public class BooleanSet extends AbstractSetHandler {

    protected  String getMethodName4JDBCWrite(){
        return "setString";
    }
    protected  int getJdbcType()
    {
        return java.sql.Types.CHAR;
    }
    
    
    

    public void init(String beanName, String fieldName, String el4ReadValue,Class<?> javaType) {
        this.javaType = javaType;
        if(el4ReadValue!=null && el4ReadValue.trim().length()>0)
        {
            this.el4Read = el4ReadValue.trim();
        }else{
            if (!javaType.isPrimitive()){
            this.el4Read = beanName.trim()+"."+Utils.getGetter(fieldName)+"()";
            }else{
                this.el4Read = beanName.trim()+".is"+Utils.getFieldNameInMethod(fieldName)+"()"; 
            }
        } 
    }
    public void wirteValue(StringBuilder sb) {
        if(this.javaType.isPrimitive())
        {
             this.wirteNotNullValue(sb);
             return;
        }
        this.checkLocal(sb);
        sb.append("if(null==").append(this.local).append("){")
            .append("  ps.").append(this.getMethodName4JDBCWrite()).append("(paramIndex++,").append(this.local).append("?\"1\":\"0\");")
            .append("}else{")
            .append("  ps.setNull(paramIndex++,").append(this.getJdbcType()).append(");")
            .append("}");
    }

    public void wirteNotNullValue(StringBuilder sb) {
        
         sb.append("ps."+this.getMethodName4JDBCWrite()+"(paramIndex++,"+this.el4Read+"?\"1\":\"0\");");
    }

    public  void wirteValueWithCheck(StringBuilder sb) {
        this.checkLocal(sb);
        sb.append("if(nul!=").append(this.local).append("){")
            .append("  ps.").append(this.getMethodName4JDBCWrite()).append("(paramIndex++,").append(this.local).append("?\"1\":\"0\");")
            .append("}");
    }
}
