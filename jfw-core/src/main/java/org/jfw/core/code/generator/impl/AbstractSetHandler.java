package org.jfw.core.code.generator.impl;

import org.jfw.core.code.generator.PreparedStatementSetHandler;

public abstract class AbstractSetHandler implements PreparedStatementSetHandler {
	private String el4Read;
	private Class<?> javaType;
	private String local = null;
	
	protected abstract String getMethodName4JDBCWrite();
	protected abstract int getJdbcType();
	
	
	

	public void init(String beanName, String fieldName, String el4ReadValue,Class<?> javaType) {
		if(el4ReadValue!=null && el4ReadValue.trim().length()>0)
		{
			this.el4Read = el4ReadValue.trim();
		}else{
			this.el4Read = beanName.trim()+"."+Utils.getGetter(fieldName)+"()";
		}
		this.javaType = javaType;
	}
	private void checkLocal(StringBuilder sb){
		if(null==this.local){
			local=Utils.getLocalVarName();
			sb.append(this.javaType.getName()).append(" ").append(this.local).append("=").append(this.el4Read).append(";");
		}
	}
	public String codeBeforWriteValue() {
//		if (this.getClass4Value().isPrimitive()) return "";
//		StringBuilder sb = new StringBuilder();
//		this.checkLocal(sb);
//		return sb.toString();
		return "";
	}

	public String wirteValue() {
		if(this.javaType.isPrimitive())
		{
			return this.wirteNotNullValue();
		}
		StringBuilder sb = new StringBuilder();
		this.checkLocal(sb);
		sb.append("if(null==").append(this.local).append("){")
		    .append("  ps.").append(this.getMethodName4JDBCWrite()).append("(paramIndex++,").append(this.local).append(");")
		    .append("}else{")
		    .append("  ps.setNull(paramIndex++,").append(this.getJdbcType()).append(");")
		    .append("}");
		return sb.toString();		
	}

	public String wirteNotNullValue() {
		
		return "ps."+this.getMethodName4JDBCWrite()+"(paramIndex++,"+this.el4Read+");";
	}

	public String wirteValueWithCheck() {
		StringBuilder sb = new StringBuilder();
		this.checkLocal(sb);
		sb.append("if(nul!=").append(this.local).append("){")
		    .append("  ps.setNull(paramIndex++,").append(this.getJdbcType()).append(");")
		    .append("}");
		return sb.toString();	
	}

	public String codeAfterWriteValue() {
		return "";
	}

	public String codeBeginCheckInSetOrWhere() {
		StringBuilder sb = new StringBuilder();
		this.checkLocal(sb);
		sb.append("if(nul!=").append(this.local).append("){");
		return sb.toString();	
	}

	public String codeELESCheckInSetOrWhere() {
		return "}else{";
	}

	public String codeEndCheckInSetOrWhere() {
		return "}";
	}

}
