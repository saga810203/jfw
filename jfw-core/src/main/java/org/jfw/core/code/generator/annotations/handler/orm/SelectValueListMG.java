package org.jfw.core.code.generator.annotations.handler.orm;

import org.jfw.core.code.generator.annotations.orm.SelectTable;
import org.jfw.core.code.generator.annotations.orm.SelectValueList;
import org.jfw.core.code.generator.enums.orm.DE;
import org.jfw.core.code.generator.orm.SelectMethodGenerator;

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
    	  if(this.dataEle.getFieldClass()!=POUtil.getPrimitiveClass(returnType)){
    	      throw new RuntimeException("invalid returnType with annotation @SelectValueList at "+this.parentType.getName()+"."+this.method.getName());
    	  }
       }
       if(returnType.isPrimitive()) returnType = POUtil.getWrapClass(returnType);
       this.classname4ReturnType = returnType.getName();
       this.fields = new SelectField[]{SelectField.build(this.dataEle)};        
       this.sql = st.sql();
    }

    @Override
    public void aferInit() {
        this.singleRow = false;
        this.needNew4ReturnType = false;
        this.resolveReturnTypeAndSql();
        this.filter = this.method.getAnnotation(SelectValueList.class).filter().trim();
        this.sqlVals =  this.method.getAnnotation(SelectValueList.class).sqlVal();
        this.sortSqlVals(); 
        this.and =  this.method.getAnnotation(SelectValueList.class).and();
        this.dynamicFilter =  this.method.getAnnotation(SelectValueList.class).dynamicFilter();
    }
}
