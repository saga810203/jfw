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
	 *  �������ݿ⽨����ֶ�������
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
	 * �Ƿ�����Ϊ��
	 */
	private boolean nullable;
	/**
	 * ��ʵ�ֶε�java����
	 */
	private Class<?> clszz;
	/**
	 * �Ƿ��������Where �����
	 */
	private boolean canFilter;
	/**
	 * �Ƿ���Ĭ�ϵ�Select����д���
	 */
	private boolean inSelect;
	/**
	 * ����ResultSetȡֵ�ı��ʽ
	 * �磺
	 *      rs.getInt(IndexDesc)
	 *      "1".equals(rs.getString(IndexDesc))      
	 *      
	 *      
	 *      ���� :IndexDesc�����滻,rs���ڷ����е�ResultSet�ľֲ�������
	 */
	private String expressionForGetter;
	/**
	 * �Ǵ���Insert�����
	 */
	private boolean inInsert;
	/**
	 * ��insert�����sql��Ĭ��ֵ
	 */
	private String defaultSqlValueForInsert;
	/**
	 * ����PreparedStatement��ֵ�ı��ʽ
	 * �磺
	 *      ps.setInt($IndexDesc,$SQLVAL)
	 *      ps.setString($IndexDesc,$SQLVAL?"1":"0")
	 *           
	 *      
	 *      ���� :$IndexDesc�����滻,ps���ڷ����е�PreparedStatement�ľֲ�������
	 */
	private String expressionForSetter;
	/**
	 * ����PreparedStatement��NULL�ı��ʽ
	 * �磺
	 *      ps.setNull($IndexDesc,java.sql.Types.INTEGER)
	 *           
	 *      
	 *      ���� :$IndexDesc�����滻,ps���ڷ����е�PreparedStatement�ľֲ�������
	 */
	private String expressionForNullSetter;
	
	private boolean inUpdate;
	
	private String defaultSqlValueForUpdate;
	
	
	
	
	
	
	
    
	

}
