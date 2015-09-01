package org.jfw.core.code.generator.orm.impl;

import org.jfw.core.code.generator.orm.PreparedStatementSetHandler;

public abstract class AbstractSetHandler implements PreparedStatementSetHandler {
	protected String el4Read;
	protected Class<?> javaType;
	protected String local = null;
	
	@Override
    public boolean isReplaceResource() {
        return false;
    }
    @Override
    public void replaceResource(StringBuilder sb) {
    }
    protected  abstract String getMethodName4JDBCWrite();
	protected  abstract int getJdbcType();
	
	
	

	public void init(String beanName, String fieldName, String el4ReadValue,Class<?> javaType) {
		if(el4ReadValue!=null && el4ReadValue.trim().length()>0)
		{
			this.el4Read = el4ReadValue.trim();
		}else{
			this.el4Read = beanName.trim()+"."+Utils.getGetter(fieldName)+"()";
		}
		this.javaType = javaType;
	}
	protected void checkLocal(StringBuilder sb){
		if(null==this.local){
			local=Utils.getLocalVarName();
			sb.append(this.javaType.getName()).append(" ").append(this.local).append("=").append(this.el4Read).append(";");
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
		    .append("  ps.").append(this.getMethodName4JDBCWrite()).append("(paramIndex++,").append(this.local).append(");")
		    .append("}else{")
		    .append("  ps.setNull(paramIndex++,").append(this.getJdbcType()).append(");")
		    .append("}");
	}

	public void wirteNotNullValue(StringBuilder sb) {
		
		 sb.append("ps."+this.getMethodName4JDBCWrite()+"(paramIndex++,"+this.el4Read+");");
	}

	public  void wirteValueWithCheck(StringBuilder sb) {
		this.checkLocal(sb);
		sb.append("if(nul!=").append(this.local).append("){")
		    .append("  ps.").append(this.getMethodName4JDBCWrite()).append("(paramIndex++,").append(this.local).append(");")
		    .append("}");
	}



	public void codeBeginCheckInSetOrWhere(StringBuilder sb) {
		this.checkLocal(sb);
		sb.append("if(nul!=").append(this.local).append("){");
	}

	public void codeELESCheckInSetOrWhere(StringBuilder sb) {
		sb.append("}else{");
	}

	public void codeEndCheckInSetOrWhere(StringBuilder sb) {
		sb.append("}");
	}

}
