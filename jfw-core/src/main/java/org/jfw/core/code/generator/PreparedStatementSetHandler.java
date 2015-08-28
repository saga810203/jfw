package org.jfw.core.code.generator;

public interface PreparedStatementSetHandler {
	void init(String beanName,String fieldName,String el4ReadValue,Class<?> javaType);
	
	//在赋值之前调用：如生成BLOB,
	String codeBeforWriteValue();
	/**
	 * 只在insert,update set a=? 中使用
	 * @return
	 */
	String wirteValue();
	/*
	 * 在where 中 
	 */
	String wirteNotNullValue();
	/*
	 *在可能不用赋值的地方使用 
	 *如动态生成SQL的赋值时用
	 */
	String wirteValueWithCheck();
	//在赋值之后调用:释放BLOB
	String codeAfterWriteValue();
	
	
	String codeBeginCheckInSetOrWhere();
	String codeELESCheckInSetOrWhere();
	String codeEndCheckInSetOrWhere();
}
