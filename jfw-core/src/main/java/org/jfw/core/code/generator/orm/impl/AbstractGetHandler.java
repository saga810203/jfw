package org.jfw.core.code.generator.orm.impl;

import org.jfw.core.code.generator.orm.ResultSetGetHandler;
/**
 * 处理简单的数据，不需数据转换，BLOB，CLOB,的处理别行处理
 * @author pengjia
 *
 */
public abstract class AbstractGetHandler implements ResultSetGetHandler {
	protected String el4Read;
	protected String el4Write=null;
	protected String el4WasNull;
	protected boolean nullable;
	protected Class<?> javaType;
	
	
	
	protected abstract String getMethodName4JDBCRead();

	public void init(int colIndex, String colName, boolean nullable,
			String fieldName,Class<?> javaType) {
		if (colIndex <= 0) {
			this.el4Read = "rs."+this.getMethodName4JDBCRead()+"(\"" + colName.trim() + "\")";
			this.el4WasNull = "rs.wasNull()";
		} else {
			this.el4Read = "rs."+this.getMethodName4JDBCRead()+"(" + colIndex + ")";
			this.el4WasNull = "rs.wasNull()";
		}
		//当fieldName为null时只读一列值
		if(fieldName!=null)this.el4Write = "obj." + Utils.getSetter(fieldName);
		this.nullable = nullable;
		this.javaType = javaType;
	}
    @Override
	public void readValue(StringBuilder sb) {
		if (this.nullable && (!this.javaType.isPrimitive())) {
		    
			String local = Utils.getLocalVarName();
            if (this.el4Write==null){
                local ="obj";
            }
			sb.append(this.javaType.getName()).append(" ").append(local).append(" =").append(this.el4Read).append(";")
			    .append("if(").append(this.el4WasNull).append("){");
			if(null!= this.el4Write){
			     sb.append(this.el4Write).append("(null);")
			     .append("}else{")
	                .append(this.el4Write).append("(").append(local).append(");}");
			}else{
			    sb.append(local).append("=null;}");
			}				
		} else {	
		    if(null!=this.el4Write){
			sb.append(this.el4Write).append("(").append(this.el4Read)	.append(");");
		    }else{
		        sb.append(this.javaType.getName()).append(" obj=").append(this.el4Read).append(";");
		    }
		}
	}

}
