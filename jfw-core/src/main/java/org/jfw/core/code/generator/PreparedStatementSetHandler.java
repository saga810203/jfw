package org.jfw.core.code.generator;

public interface PreparedStatementSetHandler {
	void init(String beanName,String fieldName,String el4ReadValue,Class<?> javaType);
	
	//�ڸ�ֵ֮ǰ���ã�������BLOB,
	void codeBeforWriteValue(StringBuilder sb);
	/**
	 * ֻ��insert,update set a=? ��ʹ��
	 * @return
	 */
	void wirteValue(StringBuilder sb);
	/*
	 * ��where �� 
	 */
	void wirteNotNullValue(StringBuilder sb);
	/*
	 *�ڿ��ܲ��ø�ֵ�ĵط�ʹ�� 
	 *�綯̬����SQL�ĸ�ֵʱ��
	 */
	void wirteValueWithCheck(StringBuilder sb);
	//�ڸ�ֵ֮�����:�ͷ�BLOB
	void codeAfterWriteValue(StringBuilder sb);
	
	
	void codeBeginCheckInSetOrWhere(StringBuilder sb);
	void codeELESCheckInSetOrWhere(StringBuilder sb);
	void codeEndCheckInSetOrWhere(StringBuilder sb);
}
