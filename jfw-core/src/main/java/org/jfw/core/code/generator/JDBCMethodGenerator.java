package org.jfw.core.code.generator;

import org.jfw.core.code.MethodGenerator;

public abstract class JDBCMethodGenerator extends MethodGenerator {
    protected boolean dynamicSql=false;



    @Override
    protected void buildContentBody(StringBuilder sb) {
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
