package org.jfw.core.code.generator.enums;
/**
 * DataElement
 * @author pengjia
 *
 */
public enum DE {
	
	
	ID("000001","CHAR",32,-1);
	
	private DE(String id,String typeForDataBase,int length,int secondlength)
	{
		this.id = id;
		this.typeForDataBase = typeForDataBase;
		this.length = length;
		this.secondlength = secondlength;
	}
	private String id;
	/**
	 *  用于数据库建表的字段描述，
	 *  if (length <0 && secondlength <0){
	 *  create table XXXXXX   columnName  typeForDataBase;
	 *  } if ( secondlength <0){
	 *  create table XXXXXX    columnName  typeForDataBase(length);
	 *  }else{
	 *  create table XXXXXX    columnName  typeForDataBase(length,secondLength);
	 *  }
	 */
	private String typeForDataBase;
	private int length;
	private int secondlength;
	/**
	 * 是否允许为空
	 */
	private boolean nullable;
	/**
	 * 对实字段的java类型
	 */
	private Class<?> clszz;
	/**
	 * 是否可以用在Where 语句中
	 */
	private boolean canFilter;
	/**
	 * 是否在默认的Select语句中存在
	 */
	private boolean inSelect;
	/**
	 * 利用ResultSet取值的表达式
	 * 如：
	 *      rs.getInt(IndexDesc)
	 *      "1".equals(rs.getString(IndexDesc))      
	 *      
	 *      
	 *      其中 :IndexDesc用于替换,rs是在方法中的ResultSet的局部变量名
	 */
	private String expressionForGetter;
	/**
	 * 是存在Insert语句中
	 */
	private boolean inInsert;
	/**
	 * 在insert语句中sql的默认值
	 */
	private String defaultSqlValueForInsert;
	/**
	 * 利用PreparedStatement赋值的表达式
	 * 如：
	 *      ps.setInt($IndexDesc,$SQLVAL)
	 *      ps.setString($IndexDesc,$SQLVAL?"1":"0")
	 *           
	 *      
	 *      其中 :$IndexDesc用于替换,ps是在方法中的PreparedStatement的局部变量名
	 */
	private String expressionForSetter;
	/**
	 * 利用PreparedStatement赋NULL的表达式
	 * 如：
	 *      ps.setNull($IndexDesc,java.sql.Types.INTEGER)
	 *           
	 *      
	 *      其中 :$IndexDesc用于替换,ps是在方法中的PreparedStatement的局部变量名
	 */
	private String expressionForNullSetter;
	
	private boolean inUpdate;
	
	private String defaultSqlValueForUpdate;
	
	
	
	
	
	
	
    
	

}
