package org.jfw.core.code.generator.annotations.handler;

import java.util.List;

import org.jfw.core.code.generator.SelectMethodGenerator;
import org.jfw.core.code.generator.annotations.SelectTable;
import org.jfw.core.code.generator.annotations.SelectView;
import org.jfw.core.code.generator.annotations.View;

public class SelectViewMG extends SelectMethodGenerator {
	protected Class<?> viewClass;
	protected String viewName ;
	protected View view;
	
	protected void resolveReturnTypeAndSql()
	{
		SelectView st = this.method.getAnnotation(SelectView.class);
		if(null== st) throw new RuntimeException("class SelectTableMG only handle method with annotation @SelectTable");
		this.viewClass = st.beanClass();
        Class<?> returnType = POUtil.getReturnType4SelectList(this.method);
        if(null== returnType) throw new RuntimeException("invalid returnType with annotation @SelectTable at "+this.parentType.getName()+"."+this.method.getName());
        if(this.viewClass == Object.class){
        	this.viewClass = returnType;
        }else{
        	if(!this.viewClass.isAssignableFrom(returnType))
        	{
        		if(returnType.isAssignableFrom(this.viewClass))
        		{
        			returnType = this.viewClass;
        		}else{
        			throw new RuntimeException("invalid returnType or annotation property(beanClass) with annotation @SelectTable at "+this.parentType.getName()+"."+this.method.getName());
        		}
        	}
        }
        this.view = POUtil.getViewAnnotation(viewClass);
        this.viewName = POUtil.GetViewName(viewClass); 
        this.classname4ReturnType = returnType.getName();

        List<SelectField> list =  POUtil.getSelectFieldsInView(this.viewClass);        
        
        if (list.isEmpty()) throw new RuntimeException("not found Query Field with @DBField in Class:"+this.viewClass.getName());
       
        if(Object.class!=view.inherited().value())
        {
            List<SelectField> list2=POUtil.getSelectFieldsInTable(view.inherited().value());
            String taAlias= view.inherited().tableAlias().trim();
            if(taAlias.length()>0)
            {
                for(SelectField sff:list2)
                {
                    sff.setDbFieldName(taAlias+"."+sff.getDbFieldName());
                }
                list.addAll(0,list2);
            }
        }
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
        sb.append(" FROM ").append(this.viewName);
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
