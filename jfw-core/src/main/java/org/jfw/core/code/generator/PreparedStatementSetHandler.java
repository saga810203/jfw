package org.jfw.core.code.generator;

import java.lang.reflect.Field;

import org.jfw.core.code.generator.annotations.DBField;
import org.jfw.core.code.generator.enums.DE;

public interface PreparedStatementSetHandler {
	void init(DE de, String expressionForGetValue);
	
	/**
	 * ���ɵĴ���(�ڶ�̬����update Sqlʱʹ��)
	 * �����ľֲ�����
	 * StringBulider usql
	 * 
	 *   String  $UUID$ = expressionForGetValue;
	 *   if(null==$UUID$)
	 *   {
	 *   	usql.append("$COLUMN_NAME$=?,");
	 *   }
	 *   
	 *   
	 */
	String codeForUpdateValueSelectiveSQL(String columnName);
	/**
	 * ͬ��
	 * $COLUMN_NAME=?,
	 * $COLUMN_NAME=""
	 */
	String codeForUpdateSQL(String columnName);
	
	String 
	
	
	
	
	

}
