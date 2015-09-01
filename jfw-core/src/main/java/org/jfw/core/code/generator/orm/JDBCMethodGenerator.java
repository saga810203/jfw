package org.jfw.core.code.generator.orm;

import org.jfw.core.code.generator.annotations.orm.SqlVal;
import org.jfw.core.code.generator.enums.orm.DE;
import org.jfw.core.code.orm.MethodGenerator;

public abstract class JDBCMethodGenerator extends MethodGenerator {
    protected boolean dynamicSql=false;
    protected SqlVal[] sqlVals=new SqlVal[0];
    protected PreparedStatementSetHandler[] psshs =new PreparedStatementSetHandler[0];
    protected SqlValue[] sqlvalues = new SqlValue[0];
    
    
    private void createSqlVaues(){
        if((this.sqlvalues.length>0)||(0==this.sqlVals.length)) return;
        this.sqlvalues = new SqlValue[this.sqlVals.length];
        for(int i = 0 ; i < this.sqlVals.length ;++i)
        {
            this.sqlvalues[i] = new SqlValue(this.sqlVals[i]);
        }
    }
    
    protected void buildPSSH(){
        this.createSqlVaues();
    	if(this.sqlvalues.length>0){
    		this.psshs = new PreparedStatementSetHandler[this.sqlvalues.length];
    		for(int i = 0 ; i < this.sqlvalues.length; ++i){
    			try {
					this.psshs[i] = this.sqlvalues[i].getDataElement().getWriteClass().newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
    		}
    	}
    }
    @Override
    protected void buildContentBody(StringBuilder sb) {
    	buildPSSH();
    	
        this.buildQuerySQL(sb);
        
        sb.append("java.sql.PreparedStatement ps = con.prepareStatement(sql");
        if(this.dynamicSql){
            sb.append(".toString()");
        }
        sb.append(");");
        sb.append("try{");
        this.buildQueryParamter(sb);
        this.buildHandleResult(sb);
        sb.append("}finally{try{ps.close();}catch(SQLException e){}}");        
    }
    protected abstract void buildQuerySQL(StringBuilder sb);
    protected abstract void buildQueryParamter(StringBuilder sb);
    protected abstract void buildHandleResult(StringBuilder sb);

    public static class SqlValue{
        private int paramIndex;;
        private String field;
        private String sqlEl;
        private DE dataElement;
        public int getParamIndex() {
            return paramIndex;
        }
        public String getField() {
            return field;
        }
        public String getSqlEl() {
            return sqlEl;
        }
        public DE getDataElement() {
            return dataElement;
        }
        public SqlValue(SqlVal sv)
        {
            this.paramIndex = sv.paramIndex();
            this.field = sv.field();
            this.dataElement = sv.dataElement();
            this.sqlEl = sv.sqlEl();
        }
        public SqlValue(int paramIndex,String field,String sqlEl,DE dataElement)
        {
            this.paramIndex = paramIndex;
            this.field = field;
            this.sqlEl = sqlEl;
            this.dataElement = dataElement;
        }
    }
}
