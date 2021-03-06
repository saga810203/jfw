package org.jfw.core.code.generator.annotations.handler.orm;

import org.jfw.core.code.generator.annotations.orm.SelectValue;
import org.jfw.core.code.generator.annotations.orm.SelectValueList;

public class SelectValueMG extends SelectValueListMG{
    protected void resolveReturnTypeAndSql()
    {
        SelectValueList st = this.method.getAnnotation(SelectValueList.class);
        if(null== st) throw new RuntimeException("class SelectValueListMG only handle method with annotation @SelectValueList");
        this.dataEle =st.resultType();
        Class<?> returnType = POUtil.getReturnType4SelectOne(this.method);
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
        this.singleRow = true;
        this.needNew4ReturnType = false;
        this.resolveReturnTypeAndSql();
        this.filter = this.method.getAnnotation(SelectValue.class).filter().trim();
        this.sqlVals =  this.method.getAnnotation(SelectValue.class).sqlVal();
        this.sortSqlVals(); 
        this.and =  this.method.getAnnotation(SelectValue.class).and();
        this.dynamicFilter =  this.method.getAnnotation(SelectValue.class).dynamicFilter();
    }
}
