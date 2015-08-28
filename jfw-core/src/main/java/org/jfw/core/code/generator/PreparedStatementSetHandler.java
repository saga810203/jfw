package org.jfw.core.code.generator;

public interface PreparedStatementSetHandler {
	void init(String beanName,String fieldName,String el4ReadValue,Class<?> javaType);
	
	//�ڸ�ֵ֮ǰ���ã�������BLOB,
	String codeBeforWriteValue();
	/**
	 * ֻ��insert,update set a=? ��ʹ��
	 * @return
	 */
	String wirteValue();
	/*
	 * ��where �� 
	 */
	String wirteNotNullValue();
	/*
	 *�ڿ��ܲ��ø�ֵ�ĵط�ʹ�� 
	 *�綯̬����SQL�ĸ�ֵʱ��
	 */
	String wirteValueWithCheck();
	//�ڸ�ֵ֮�����:�ͷ�BLOB
	String codeAfterWriteValue();
	
	
	String codeBeginCheckInSetOrWhere();
	String codeELESCheckInSetOrWhere();
	String codeEndCheckInSetOrWhere();
}
