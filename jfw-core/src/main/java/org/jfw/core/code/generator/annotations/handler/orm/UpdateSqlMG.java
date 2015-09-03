package org.jfw.core.code.generator.annotations.handler.orm;

import org.jfw.core.code.generator.annotations.orm.UpdateSql;
import org.jfw.core.code.generator.orm.UpdateMethodGenerator;

public class UpdateSqlMG extends UpdateMethodGenerator{
    private UpdateSql us;

    @Override
    protected void buildQueryParamter(StringBuilder sb) {
       for(int i = 0 ; i < this.us.sqlvalue().length ; ++i)
       {
           this.psshs[i].wirteValue(sb);
       }
       for(int i = this.us.sqlvalue().length ; i < this.sqlvalues.length ; ++i)
       {
           this.psshs[i].wirteNotNullValue(sb);
       }
    }

    @Override
    protected void buildSQL(StringBuilder sb) {
        sb.append("String sql=\"").append(this.us.value()).append("\";");
        
    }

    @Override
    public void aferInit() {
        this.dynamicSql = false;     
        this.checkReturnType();
        this.us = this.method.getAnnotation(UpdateSql.class);
        if (null == us)
            throw new RuntimeException("not found @UpdateSql at method: " + this.parentType.getName() + "."
                    + this.method.getName());
        this.sqlvalues = new SqlValue[this.us.sqlvalue().length+this.us.where().length];
        for(int i = 0 ; i < this.us.sqlvalue().length ; ++i)
        {
            this.sqlvalues[i] = new SqlValue(this.us.sqlvalue()[i]);
        }
        for(int i = this.us.sqlvalue().length ; i < this.sqlvalues.length ; ++i)
        {
            this.sqlvalues[i] = new SqlValue(this.us.where()[i-this.us.sqlvalue().length]);
        }
        
    }

}
