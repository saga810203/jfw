package org.jfw.core.code.generator.annotations.handler.orm;

import org.jfw.core.code.generator.annotations.orm.InsertTable;
import org.jfw.core.code.generator.orm.UpdateMethodGenerator;

public class InsertTableMG extends UpdateMethodGenerator{
	private InsertTable it;
	
	

	@Override
	protected void buildQuerySQL(StringBuilder sb) {
		// TODO Auto-generated method stub
		
	}
	
	private Class<?> getTableClassFromParameters()
	{
		Class<?>[] cls = this.method.getParameterTypes();
		if(cls.length<1 || cls.length>2) throw new RuntimeException("invalid args with annotation @InsertTable at method:"+this.parentType.getName()+"."+this.method.getName());
		if(cls.length==1) return cls[0];
		if(cls[0]== java.sql.Connection.class) return cls[1];
		if(cls[1]!=java.sql.Connection.class) throw new RuntimeException("invalid args with annotation @InsertTable at method:"+this.parentType.getName()+"."+this.method.getName());
		return cls[0];
	}
	
	private void initInsertField(Class<?> clazz)
	{
		
	}


	@Override
	public void aferInit() {
		this.checkReturnType();
		this.it = this.method.getAnnotation(InsertTable.class);
		if(null==it) throw new RuntimeException("not found @InsertTable at method: "+this.parentType.getName()+"."+this.method.getName());
		this.dynamicSql = it.dynamic();
		Class<?>  anCls = it.value();
		Class<?>   paCls= getTableClassFromParameters();
		if(anCls==Object.class)
		{
			anCls = paCls;
		}
		if(!anCls.isAssignableFrom(paCls)){
			throw new RuntimeException("invalid args or @InsertTable.value: "+this.parentType.getName()+"."+this.method.getName());
		}
		
		
		
		
		
		
		
		
		
		
		
	}


}
