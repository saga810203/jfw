package org.jfw.core.code.generator.enums;

import org.jfw.core.code.generator.PreparedStatementSetHandler;
import org.jfw.core.code.generator.ResultSetGetHandler;
import org.jfw.core.code.generator.impl.StringGet;
import org.jfw.core.code.generator.impl.StringSet;

/**
 * DataElement
 * 
 * @author pengjia
 *
 */
public enum DE {

	STRING_VARCHAR("DA0001", "CHAR", 255, -1,true,true,true,true,null,true,null,StringGet.class,StringSet.class,String.class),
	NN_STRING_VARCHAR("DA0002", "CHAR", 255, -1,false,true,true,true,null,true,null,StringGet.class,StringSet.class,String.class);
//	int_INTEGER

	private DE(String id, String typeForDataBase, int length, int secondlength,
			boolean nullable,boolean canFilter,
			boolean inSelect, boolean inInsert,
			String defaultSqlValueForInsert, boolean inUpdate,
			String defaultSqlValueForUpdate, Class<? extends ResultSetGetHandler> readClass,
			Class<? extends PreparedStatementSetHandler> writeClass,Class<?> fieldClass) {
		this.id = id;
		this.typeForDataBase = typeForDataBase;
		this.length = length;
		this.secondlength = secondlength;
		this.nullable = nullable;
		this.canFilter = canFilter;
		this.inSelect = inSelect;
		this.inInsert = inInsert;
		this.defaultSqlValueForInsert = defaultSqlValueForInsert;
		this.inUpdate = inUpdate;
		this.defaultSqlValueForUpdate = defaultSqlValueForUpdate;
		this.readClass= readClass;
		this.writeClass = writeClass;
		this.fieldClass = fieldClass;
	}
	private String id;
	/**
	 * 用于数据库建表的字段描述， if (length <0 && secondlength <0){ create table XXXXXX
	 * columnName typeForDataBase; } if ( secondlength <0){ create table XXXXXX
	 * columnName typeForDataBase(length); }else{ create table XXXXXX columnName
	 * typeForDataBase(length,secondLength); }
	 */
	private String typeForDataBase;
	private int length;
	private int secondlength;
	/**
	 * 是否允许为空
	 */
	private boolean nullable;
	/**
	 * 是否可以用在Where 语句中
	 */
	private boolean canFilter;
	/**
	 * 是否在默认的Select语句中存在
	 */
	private boolean inSelect;
	/**
	 * 是存在Insert语句中
	 */
	private boolean inInsert;
	/**
	 * 在insert语句中sql的默认值
	 */
	private String defaultSqlValueForInsert;
	private boolean inUpdate;
	private String defaultSqlValueForUpdate;
	private Class<? extends ResultSetGetHandler> readClass;
	private Class<? extends PreparedStatementSetHandler> writeClass;
	private Class<?> fieldClass;
	
	public Class<?> getFieldClass() {
		return fieldClass;
	}
	public String getId() {
		return id;
	}
	public String getTypeForDataBase() {
		return typeForDataBase;
	}
	public int getLength() {
		return length;
	}
	public int getSecondlength() {
		return secondlength;
	}
	public boolean isNullable() {
		return nullable;
	}
	public boolean isCanFilter() {
		return canFilter;
	}
	public boolean isInSelect() {
		return inSelect;
	}
	public boolean isInInsert() {
		return inInsert;
	}
	public String getDefaultSqlValueForInsert() {
		return defaultSqlValueForInsert;
	}
	public boolean isInUpdate() {
		return inUpdate;
	}
	public String getDefaultSqlValueForUpdate() {
		return defaultSqlValueForUpdate;
	}
	public Class<? extends ResultSetGetHandler> getReadClass() {
		return readClass;
	}
	public Class<? extends PreparedStatementSetHandler> getWriteClass() {
		return writeClass;
	}
}
