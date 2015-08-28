package org.jfw.core.code.generator;


public interface ResultSetGetHandler {	
	void init(int colIndex,String colName,boolean nullable,String fieldName,Class<?> javaType);
	String readValue();
}
