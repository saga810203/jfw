package org.jfw.core.code.generator;

import java.lang.reflect.Field;

import org.jfw.core.code.generator.annotations.DBField;
import org.jfw.core.code.generator.enums.DE;

public interface PreparedStatementSetHandler {
	void init(DE de, String expressionForGetValue);
	
	/**
	 * 生成的代码(在动态生成update Sql时使用)
	 * 依赖的局部变量
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
	 * 同上
	 * $COLUMN_NAME=?,
	 * $COLUMN_NAME=""
	 */
	String codeForUpdateSQL(String columnName);
	
	String 
	
	
	
	
	

}
