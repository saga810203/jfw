package org.jfw.core.code.generator;

import org.jfw.core.code.generator.annotations.SqlVal;
import org.jfw.core.code.generator.annotations.handler.SelectField;

public abstract class SelectMethodGenerator extends JDBCMethodGenerator {
    protected String sql;
    protected String filter;
    protected String order;
    protected SqlVal[] sqlVals;
    protected boolean and;
    protected boolean dynamicFilter;  
    
    
    protected SelectField[] fields;
    protected PreparedStatementSetHandler[] psshs;
    
    
    protected abstract void initParameter4Query();
    
    private String getAndOr()
    {
        return this.and?" AND ":" OR ";
    }
    private void checkSqlVals()
    {
       if ((this.filter.length() >0) || (this.sqlVals.length==0)) return ;
       for(int i = 0 ; i < this.sqlVals.length; ++i)
       {
           if(this.sqlVals[i].sqlEl().trim().length()==0) 
           {
               throw new IllegalArgumentException("invalid annotation value sqlVal (reason: sqlEl is empyt) in Mehtod:"+this.parentType.getName()+"."+this.method.getName());
           }
       }
    }
    
    protected void buildQuerySQLWithDynamic(StringBuilder sb)
    {
        sb.append("StringBuilder sql = new StringBuilder();")
           .append(" sql.append(\"").append(this.sql).append(" WHERE ");
        if(this.and){
            sb.append("1=1");
        }else{
            sb.append("1=2");
        }
        sb.append("\";");
     
       for(int i = 0 ; i < sqlVals.length; ++i)
       {
          if(this.sqlVals[i].dataElement().getFieldClass().isPrimitive()){
              sb.append("sql.append(\"").append(this.getAndOr()).append(this.sqlVals[i].sqlEl())
                 .append("\");");
          }else{
              this.psshs[i].codeBeginCheckInSetOrWhere(sb);
              sb.append("sql.append(\"").append(this.getAndOr()).append(this.sqlVals[i].sqlEl())
              .append("\");");
              this.psshs[i].codeEndCheckInSetOrWhere(sb);
          }
       }
        if(null!=this.order&& this.order.length()>0){
            sb.append(" ORDER BY ").append(this.order);
        }  
        sb.append("\";");       
    }

    @Override
    protected void buildQuerySQL(StringBuilder sb) {
        //TODO: esc String with sql,filter ,order;
        this.initParameter4Query();
        this.checkSqlVals();
        this.dynamicSql=this.dynamicFilter && sqlVals.length>0 && (this.filter.length()==0);
        if(this.dynamicSql){
            this.buildQuerySQLWithDynamic(sb);
            return;
        }
        sb.append("String sql = \"").append(this.sql);
        if(filter.length()>0){
            sb.append(" WHERE ").append(this.filter);
        }else if(sqlVals.length>0)
        {
           sb.append(" WHERE ");
           for(int i = 0 ; i < sqlVals.length; ++i)
           {
               if(i!=0){
                   if(this.and){
                       sb.append(" AND ");
                   }else{
                       sb.append(" OR ");
                   }
               }
               sb.append(sqlVals[i].sqlEl());
           }
        }
        if(null!=this.order&& this.order.length()>0){
            sb.append(" ORDER BY ").append(this.order);
        }  
        sb.append("\";");       
    }

    @Override
    protected void buildQueryParamter(StringBuilder sb) {
        sb.append("int paramIndex = 1;");
       if(this.dynamicSql)
       {
           for(int i = 0 ; i < this.psshs.length ; ++i){
               this.psshs[i].wirteValueWithCheck(sb);
           }
       }else{
           for(int i = 0 ; i < this.psshs.length ; ++i)
           {
               this.psshs[i].wirteNotNullValue(sb);
           }
       }
        
    }

    @Override
    protected void buildHandleResult(StringBuilder sb) {
       sb.append("java.sql.ResultSet rs = ps.executeQuery();try{");
       this.buildHandleResultInternal(sb);
       sb.append("}finaly{try{rs.close();}catch(SQLException e){}}");        
    }
    protected abstract void buildHandleResultInternal(StringBuilder sb);

}
