package org.jfw.core.code.generator.annotations.handler.orm;

import java.util.List;

import org.jfw.core.code.generator.annotations.orm.SelectTable;
import org.jfw.core.code.generator.annotations.orm.Table;
import org.jfw.core.code.generator.orm.SelectMethodGenerator;

public class SelectTableMG extends SelectMethodGenerator {
	protected Class<?> tableClass;
	protected String tableName ;
	protected Table table;
	
	
	protected void resolveReturnTypeAndSql()
	{
		SelectTable st = this.method.getAnnotation(SelectTable.class);
		if(null== st) throw new RuntimeException("class SelectTableMG only handle method with annotation @SelectTable");
		this.tableClass = st.beanClass();
        Class<?> returnType = POUtil.getReturnType4SelectList(this.method);
        if(null== returnType) throw new RuntimeException("invalid returnType with annotation @SelectTable at "+this.parentType.getName()+"."+this.method.getName());
        if(this.tableClass == Object.class){
        	this.tableClass = returnType;
        }else{
        	if(!this.tableClass.isAssignableFrom(returnType))
        	{
        		if(returnType.isAssignableFrom(this.tableClass))
        		{
        			returnType = this.tableClass;
        		}else{
        			throw new RuntimeException("invalid returnType or annotation property(beanClass) with annotation @SelectTable at "+this.parentType.getName()+"."+this.method.getName());
        		}
        	}
        }
        this.table = POUtil.getTableAnnotation(tableClass);
        this.tableName = POUtil.GetTableName(tableClass); 
        this.classname4ReturnType = returnType.getName();

        List<SelectField> list =  POUtil.getSelectFieldsInTable(this.tableClass);        
        if (list.isEmpty()) throw new RuntimeException("not found Query Field with @DBField in Class:"+this.tableClass.getName());
        this.fields = list.toArray(new SelectField[list.size()]);
        
        StringBuilder sb =new StringBuilder();
        sb.append("SELECT ");
        for(int i = 0 ; i < this.fields.length; ++i )
        {
        	if(0!=i)sb.append(",");
        	sb.append(this.fields[i].getDbFieldName());
            if(this.fields[i].getDbFieldAlias().length()>0){
                sb.append(" ").append(this.fields[i].getDbFieldAlias());
            }
        }
        sb.append(" FROM ").append(this.tableName);
        this.sql = sb.toString();
	}

	@Override
	public void aferInit() {
		this.singleRow = false;
		this.needNew4ReturnType = true;
		this.resolveReturnTypeAndSql();
		this.filter = this.method.getAnnotation(SelectTable.class).filter();
		this.filter = null== this.filter?"":this.filter.trim();
		this.order =  this.method.getAnnotation(SelectTable.class).order();
		this.order = null== this.order?"":this.order.trim();
		this.sqlVals =  this.method.getAnnotation(SelectTable.class).sqlVal();
		this.sortSqlVals();		
        this.and =  this.method.getAnnotation(SelectTable.class).and();
        this.dynamicFilter =  this.method.getAnnotation(SelectTable.class).dynamicFilter();
 	}

}
