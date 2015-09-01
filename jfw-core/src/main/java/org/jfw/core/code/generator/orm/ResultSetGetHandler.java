package org.jfw.core.code.generator.orm;


public interface ResultSetGetHandler {	
	void init(int colIndex,String colName,boolean nullable,String fieldName,Class<?> javaType);
	void readValue(StringBuilder sb);
}
