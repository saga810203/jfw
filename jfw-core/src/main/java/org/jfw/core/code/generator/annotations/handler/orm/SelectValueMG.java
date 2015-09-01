package org.jfw.core.code.generator.annotations.handler.orm;

import org.jfw.core.code.generator.annotations.orm.SelectTable;
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
       super.aferInit();
       this.singleRow = true;
    }
}
