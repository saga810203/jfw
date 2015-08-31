package org.jfw.core.code.generator.annotations.handler;

import java.util.List;

import org.jfw.core.code.generator.annotations.SelectTableRow;

public class SelectTableRowMG extends SelectTableMG {

	@Override
	public void aferInit() {
		super.aferInit();
		this.singleRow = true;
	}
	protected void resolveReturnTypeAndSql()
	{
		SelectTableRow st = this.method.getAnnotation(SelectTableRow.class);
		if(null== st) throw new RuntimeException("class SelectTableRowMG only handle method with annotation @SelectTableRow");
		this.tableClass = st.beanClass();
        Class<?> returnType = POUtil.getReturnType4SelectOne(this.method);
        if(null== returnType) throw new RuntimeException("invalid returnType with annotation @SelectTableRow at "+this.parentType.getName()+"."+this.method.getName());
        if(this.tableClass == Object.class){
        	this.tableClass = returnType;
        }else{
        	if(!this.tableClass.isAssignableFrom(returnType))
        	{
        		if(returnType.isAssignableFrom(this.tableClass))
        		{
        			returnType = this.tableClass;
        		}else{
        			throw new RuntimeException("invalid returnType or annotation property(beanClass) with annotation @SelectTableRow at "+this.parentType.getName()+"."+this.method.getName());
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

}
