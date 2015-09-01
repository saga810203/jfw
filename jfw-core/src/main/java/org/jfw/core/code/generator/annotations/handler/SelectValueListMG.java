package org.jfw.core.code.generator.annotations.handler;

import java.util.List;

import org.jfw.core.code.generator.SelectMethodGenerator;
import org.jfw.core.code.generator.annotations.SelectTable;
import org.jfw.core.code.generator.annotations.SelectValueList;
import org.jfw.core.code.generator.annotations.Table;
import org.jfw.core.code.generator.enums.DE;

public class SelectValueListMG  extends SelectMethodGenerator{
    protected DE dataEle;
    
    protected void resolveReturnTypeAndSql()
    {
        SelectValueList st = this.method.getAnnotation(SelectValueList.class);
        if(null== st) throw new RuntimeException("class SelectValueListMG only handle method with annotation @SelectValueList");
        this.dataEle =st.resultType();
        Class<?> returnType = POUtil.getReturnType4SelectList(this.method);
        if(null== returnType) throw new RuntimeException("invalid returnType with annotation @SelectValueList at "+this.parentType.getName()+"."+this.method.getName());
       if(Object.class==returnType){
    	   returnType = this.dataEle.getFieldClass();
       }
       if(returnType!=this.dataEle.getFieldClass()){
    	   if()
       }
        
        
        
        
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
    }

    @Override
    public void aferInit() {
        this.singleRow = false;
        this.needNew4ReturnType = false;
        this.resolveReturnTypeAndSql();
        this.filter = this.parentType.getAnnotation(SelectTable.class).filter().trim();
        this.order =  this.parentType.getAnnotation(SelectTable.class).order().trim();
        this.sqlVals =  this.parentType.getAnnotation(SelectTable.class).sqlVal();
        this.and =  this.parentType.getAnnotation(SelectTable.class).and();
        this.dynamicFilter =  this.parentType.getAnnotation(SelectTable.class).dynamicFilter();
    }
}
