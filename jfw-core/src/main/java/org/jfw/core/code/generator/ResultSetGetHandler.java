package org.jfw.core.code.generator;


public interface ResultSetGetHandler {	
	void init(int colIndex,String colName,boolean nullable,String fieldName,String className);
	String codeBeforReadValue();
	String readValue();
	String codeAfterReadValue();
}
