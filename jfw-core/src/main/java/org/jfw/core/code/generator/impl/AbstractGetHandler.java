package org.jfw.core.code.generator.impl;

import org.jfw.core.code.generator.ResultSetGetHandler;
/**
 * 处理简单的数据，不需数据转换，BLOB，CLOB,的处理别行处理
 * @author pengjia
 *
 */
public abstract class AbstractGetHandler implements ResultSetGetHandler {
	private String el4Read;
	private String el4Write;
	private String el4WasNull;
	private boolean nullable;
	private Class<?> javaType;
	
	
	
	protected abstract String getMethodName4JDBCRead();

	/**
	 * 在处理BLOB,CLOB等数据类型时需要处理资源释放
	 * @return
	 */
	//protected abstract boolean needReaplaceResource();
	//protected abstract String reaplaceResource();
	
	
	

	public void init(int colIndex, String colName, boolean nullable,
			String fieldName,Class<?> javaType) {
		if (colIndex <= 0) {
			this.el4Read = "rs."+this.getMethodName4JDBCRead()+"(\"" + colName.trim() + "\")";
			this.el4WasNull = "rs.wasNull(\"" + colName.trim() + "\")";
		} else {
			this.el4Read = "rs."+this.getMethodName4JDBCRead()+"(" + colIndex + ")";
			this.el4WasNull = "rs.wasNull(" + colIndex + ")";
		}
		this.el4Write = "obj." + Utils.getSetter(fieldName);
		this.nullable = nullable;
		this.javaType = javaType;
	}
    @Override
	public void readValue(StringBuilder sb) {
		if (this.nullable && (!this.javaType.isPrimitive())) {
			String local = Utils.getLocalVarName();
			sb.append(this.javaType.getName()).append(" ").append(local).append(" =").append(this.el4Read).append(";")
			    .append("if(").append(this.el4WasNull).append("){")
			    .append(this.el4Write).append("(null);")
				.append("}else{")
				.append(this.el4Write).append("(").append(local).append(");}");
		} else {			
			sb.append(this.el4Write).append("(").append(this.el4Read)	.append(");");
		}
	}

}
