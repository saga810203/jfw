package org.jfw.core.code.generator.orm;

public interface PreparedStatementSetHandler {
void init(String beanName,String fieldName,String el4ReadValue,Class<?> javaType,boolean nullable);
	boolean isNullable();
	//赋后是否需要释放资源：如BLOB,
    boolean isReplaceResource();
	/**
	 * 只在insert,update set a=? 中使用
	 * @return
	 */
	void wirteValue(StringBuilder sb);
	/*
	 * 在where 中 
	 */
	void wirteNotNullValue(StringBuilder sb);
	/*
	 *在可能不用赋值的地方使用 
	 *如动态生成SQL的赋值时用
	 */
	void wirteValueWithCheck(StringBuilder sb);
	//在赋值之后调用:释放BLOB
	void replaceResource(StringBuilder sb);
	void codeBeginCheckInSetOrWhere(StringBuilder sb);
	void codeELESCheckInSetOrWhere(StringBuilder sb);
	void codeEndCheckInSetOrWhere(StringBuilder sb);
}
