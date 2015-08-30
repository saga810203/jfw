package org.jfw.core.code.generator.enums;

import org.jfw.core.code.generator.PreparedStatementSetHandler;
import org.jfw.core.code.generator.ResultSetGetHandler;
import org.jfw.core.code.generator.impl.BigDecimalGet;
import org.jfw.core.code.generator.impl.BigDecimalSet;
import org.jfw.core.code.generator.impl.BooleanGet;
import org.jfw.core.code.generator.impl.BooleanSet;
import org.jfw.core.code.generator.impl.ByteGet;
import org.jfw.core.code.generator.impl.ByteSet;
import org.jfw.core.code.generator.impl.DoubleGet;
import org.jfw.core.code.generator.impl.DoubleSet;
import org.jfw.core.code.generator.impl.FloatGet;
import org.jfw.core.code.generator.impl.FloatSet;
import org.jfw.core.code.generator.impl.IntGet;
import org.jfw.core.code.generator.impl.IntSet;
import org.jfw.core.code.generator.impl.LongGet;
import org.jfw.core.code.generator.impl.LongSet;
import org.jfw.core.code.generator.impl.ShortGet;
import org.jfw.core.code.generator.impl.ShortSet;
import org.jfw.core.code.generator.impl.StringGet;
import org.jfw.core.code.generator.impl.StringSet;

/**
 * DataElement
 * 
 * @author pengjia
 *
 */
public enum DE {

    Byte("DA0005", "INTEGER", -1, -1,false,true,true,true,null,true,null,ByteGet.class,ByteSet.class,byte.class),
    BYTE("DA0006", "INTEGER", -1, -1,true,true,true,true,null,true,null,ByteGet.class,ByteSet.class,Byte.class),
    Short("DA0003", "SMALLINT", -1, -1,false,true,true,true,null,true,null,ShortGet.class,ShortSet.class,short.class),
    SHORT("DA0004", "SMALLINT", -1, -1,true,true,true,true,null,true,null,ShortGet.class,ShortSet.class,Short.class),
	integer("DA0003", "INTEGER", -1, -1,false,true,true,true,null,true,null,IntGet.class,IntSet.class,int.class),
	INTEGER("DA0004", "INTEGER", -1, -1,true,true,true,true,null,true,null,IntGet.class,IntSet.class,Integer.class),
	Long("DA0003", "BIGINT", -1, -1,false,true,true,true,null,true,null,LongGet.class,LongSet.class,long.class),
	LONG("DA0004", "BIGINT", -1, -1,true,true,true,true,null,true,null,LongGet.class,LongSet.class,Long.class),
	Float("DA0003", "FLOAT", -1, -1,false,true,true,true,null,true,null,FloatGet.class,FloatSet.class,float.class),
    FLOAT("DA0004", "FLOAT", -1, -1,true,true,true,true,null,true,null,FloatGet.class,FloatSet.class,Float.class),
    Double("DA0003", "DOUBLE", -1, -1,false,true,true,true,null,true,null,DoubleGet.class,DoubleSet.class,double.class),
    DOUBLE("DA0004", "DOUBLE", -1, -1,true,true,true,true,null,true,null,DoubleGet.class,DoubleSet.class,Double.class),    
  
    BigDecimal("DA0003", "REAL", -1, -1,false,true,true,true,null,true,null,BigDecimalGet.class,BigDecimalSet.class,java.math.BigDecimal.class),
    BIGDECIMAL("DA0004", "REAL", -1, -1,true,true,true,true,null,true,null,BigDecimalGet.class,BigDecimalSet.class,java.math.BigDecimal.class),   
    
    BOOL("DA0001", "CHAR", 1, -1,true,true,true,true,null,true,null,BooleanGet.class,BooleanSet.class,boolean.class),
    Bool("DA0002", "CHAR", 1, -1,false,true,true,true,null,true,null,BooleanGet.class,BooleanSet.class,Boolean.class),
    

    STRING("DA0001", "CHAR", 255, -1,true,true,true,true,null,true,null,StringGet.class,StringSet.class,String.class),
    string("DA0002", "CHAR", 255, -1,false,true,true,true,null,true,null,StringGet.class,StringSet.class,String.class);
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
