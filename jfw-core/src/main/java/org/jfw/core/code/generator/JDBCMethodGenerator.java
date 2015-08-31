package org.jfw.core.code.generator;

import org.jfw.core.code.MethodGenerator;
import org.jfw.core.code.generator.annotations.SqlVal;

public abstract class JDBCMethodGenerator extends MethodGenerator {
    protected boolean dynamicSql=false;
    protected SqlVal[] sqlVals=new SqlVal[0];
    protected PreparedStatementSetHandler[] psshs =new PreparedStatementSetHandler[0];
    
    protected void buildPSSH(){
    	if(this.sqlVals.length>0){
    		this.psshs = new PreparedStatementSetHandler[this.sqlVals.length];
    		for(int i = 0 ; i < this.sqlVals.length; ++i){
    			try {
					this.psshs[i] = this.sqlVals[i].dataElement().getWriteClass().newInstance();
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


}
