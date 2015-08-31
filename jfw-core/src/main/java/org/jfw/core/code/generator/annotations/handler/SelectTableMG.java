package org.jfw.core.code.generator.annotations.handler;

import java.util.List;

import org.jfw.core.code.generator.SelectMethodGenerator;
import org.jfw.core.code.generator.annotations.SelectTable;
import org.jfw.core.code.generator.annotations.Table;

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
        }
        sb.append(" FROM ").append(this.tableName);
	}

	@Override
	public void aferInit() {
		this.singleRow = false;
		this.needNew4ReturnType = true;
		this.resolveReturnTypeAndSql();
		this.filter = this.parentType.getAnnotation(SelectTable.class).filter().trim();
		this.order =  this.parentType.getAnnotation(SelectTable.class).order().trim();
		this.sqlVals =  this.parentType.getAnnotation(SelectTable.class).sqlVal();
        this.and =  this.parentType.getAnnotation(SelectTable.class).and();
        this.dynamicFilter =  this.parentType.getAnnotation(SelectTable.class).dynamicFilter();
 	}

}
