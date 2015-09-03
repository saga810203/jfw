package org.jfw.core.code.generator.annotations.handler.orm;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jfw.core.code.generator.annotations.orm.DBField;
import org.jfw.core.code.generator.annotations.orm.UpdateTable;
import org.jfw.core.code.generator.orm.UpdateMethodGenerator;

public class UpdateTableMG extends UpdateMethodGenerator {
    private UpdateTable ut;
    private String tableName;
    protected Field[] fields;
    private Map<String, String> fixSqlValue = new HashMap<String, String>();
    private int index4Where = -1;
   

    @Override
    protected void buildQueryParamter(StringBuilder sb) {
        for(int i = 0 ; i < this.index4Where ; ++i){
            if (this.dynamicSql ){
                this.psshs[i].wirteValueWithCheck(sb);
            }else{
                this.psshs[i].wirteValue(sb);
            }
        }
        for(int i = this.index4Where ; i < this.psshs.length; ++i)
        {
            this.psshs[i].wirteNotNullValue(sb);
        }

    }
    
    private void buildDynamicSQLWithHasFixSqlValue(StringBuilder sb)
    {
        boolean addCama = false;
        sb.append("StringBuilder sql = new StringBuilder();sql.append(\"UPDATE ")
           .append(this.tableName).append(" SET ");
        for (Map.Entry<String,String> entry:fixSqlValue.entrySet())
        {
            if(addCama) sb.append(",");
            addCama=true;
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        int index4DynaValue=0;
        for(int i = 0 ; i < this.index4Where ; ++i)
        {
            if(!this.sqlvalues[i].getDataElement().getFieldClass().isPrimitive()){
                index4DynaValue= i;
                break;
            }
            sb.append(",").append(this.sqlvalues[i].getSqlEl()).append("=?");
        }
        sb.append("\");");
        
        
        for(int i =index4DynaValue ; i < this.index4Where ;  ++i)
        {
           this.psshs[i].codeBeginCheckInSetOrWhere(sb);
           sb.append("sql.append(,").append(this.sqlvalues[i].getSqlEl()).append("=?\");");
           this.psshs[i].codeEndCheckInSetOrWhere(sb);   
        }
    }
    private void buildDynamicSQLWithHasPrimitiveValue(StringBuilder sb)
    {
        sb.append("StringBuilder sql = new StringBuilder();sql.append(\"UPDATE ")
           .append(this.tableName).append(" SET ");
        int index4DynaValue=0;
        for(int i = 0 ; i < this.index4Where ; ++i)
        {
            if(!this.sqlvalues[i].getDataElement().getFieldClass().isPrimitive()){
                index4DynaValue= i;
                break;
            }
            if(i!=0) sb.append(",");
            sb.append(this.sqlvalues[i].getSqlEl()).append("=?");
        }
        sb.append("\");");
        for(int i =index4DynaValue ; i < this.index4Where ;  ++i)
        {
           this.psshs[i].codeBeginCheckInSetOrWhere(sb);
           sb.append("sql.append(,").append(this.sqlvalues[i].getSqlEl()).append("=?\");");
           this.psshs[i].codeEndCheckInSetOrWhere(sb);   
        }
    }
    private void buildDynamicSQLWithHasNotPrimitiveValue(StringBuilder sb)
    {
        sb.append("StringBuilder sql = new StringBuilder();sql.append(\"UPDATE ")
           .append(this.tableName).append(" SET  \");");
        
        for(int i =0 ; i < this.index4Where ;  ++i)
        {
           this.psshs[i].codeBeginCheckInSetOrWhere(sb);
           sb.append("sql.append(,").append(this.sqlvalues[i].getSqlEl()).append("=?,\");");
           this.psshs[i].codeEndCheckInSetOrWhere(sb);   
        }
        sb.append("sql.replace(sql.length()-1,sql.length(),\" \");");
    }
    
    
    private void buildDynamicSQL(StringBuilder sb){
        if(this.fixSqlValue.size()>0){
            this.buildDynamicSQLWithHasFixSqlValue(sb);
        }else{
            if(this.sqlvalues[0].getDataElement().getFieldClass().isPrimitive()){
                this.buildDynamicSQLWithHasPrimitiveValue(sb);
            }else
            {
                buildDynamicSQLWithHasNotPrimitiveValue(sb);
            }
            
        }
        sb.append("sql.append(\" WHERE ");
        for(int i = this.index4Where ; i < this.psshs.length ; ++i)
        {
            if (i!= this.index4Where) sb.append(" AND ");
            sb.append(this.sqlvalues[i].getSqlEl()).append("=?");
        }
        sb.append("\");");
    }
    @Override
    protected void buildSQL(StringBuilder sb) {
        if(this.dynamicSql){
            buildDynamicSQL(sb);
        }else{
            sb.append("String sql=\"");
            boolean addCama = false;
            for (Map.Entry<String,String> entry:fixSqlValue.entrySet())
            {
                if(addCama) sb.append(",");
                addCama=true;
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
            for(int i = 0 ; i < this.index4Where ;  ++i)
            {
                if(addCama) sb.append(",");
                addCama=true;
                sb.append(this.sqlvalues[i].getSqlEl()).append("=").append("?");
            }
            sb.append(" WHERE ");
            for(int i = this.index4Where ; i < this.psshs.length ; ++i)
            {
                if (i!= this.index4Where) sb.append(" AND ");
                sb.append(this.sqlvalues[i].getSqlEl()).append("=?");
            }
            sb.append("\";");
        }
    }

    private Class<?> getTableClassFromParameters() {
        Class<?>[] cls = this.method.getParameterTypes();
        if (cls.length < 1 || cls.length > 2)
            throw new RuntimeException("invalid args with annotation @TableTable at method:"
                    + this.parentType.getName() + "." + this.method.getName());
        if (cls.length == 1)
            return cls[0];
        if (cls[0] == java.sql.Connection.class)
            return cls[1];
        if (cls[1] != java.sql.Connection.class)
            throw new RuntimeException("invalid args with annotation @UpdateTable at method:"
                    + this.parentType.getName() + "." + this.method.getName());
        return cls[0];
    }

    @Override
    public void aferInit() {
        this.checkReturnType();
        this.ut = this.method.getAnnotation(UpdateTable.class);
        if (null == ut)
            throw new RuntimeException("not found @UpdateTable at method: " + this.parentType.getName() + "."
                    + this.method.getName());
        Class<?> anCls = ut.value();
        Class<?> paCls = getTableClassFromParameters();
        if (anCls == Object.class) {
            anCls = paCls;
        }
        if (!anCls.isAssignableFrom(paCls)) {
            throw new RuntimeException("invalid args or @UpdateTable.value: " + this.parentType.getName() + "."
                    + this.method.getName());
        }
        this.tableName = POUtil.GetTableName(anCls);
        this.fields = POUtil.getUpdateFieldInTable(anCls).toArray(new Field[0]);
        boolean allPrimitive=true;
        LinkedList<SqlValue> svs = new LinkedList<SqlValue>();
        LinkedList<SqlValue> svwhere = new LinkedList<SqlValue>();
        for (int i = 0; i < this.fields.length; ++i) {
            DBField df = this.fields[i].getAnnotation(DBField.class);
            if(df.primaryKey())
            {
                svwhere.add(new SqlValue(1, this.fields[i].getName(), df.name(), df.value()));
                continue;
            }
            String iv = df.value().getDefaultSqlValueForUpdate();
            if (null != iv && (iv.trim().length() > 0)) {
                this.fixSqlValue.put(df.name(), iv.trim());
            } else {
                if (df.value().getFieldClass().isPrimitive()) {
                    svs.addFirst(new SqlValue(1, this.fields[i].getName(), df.name(), df.value()));
                } else {
                    svs.addLast(new SqlValue(1, this.fields[i].getName(), df.name(), df.value()));
                    allPrimitive = false;
                }
            }
        }
        if(svwhere.size()==0) throw new RuntimeException("not found primary key in table "+this.tableName);
        this.index4Where = svs.size();
        this.dynamicSql = ut.dynamic() && (!allPrimitive);
        Collections.sort(svs,new Comparator<SqlValue>(){

            @Override
            public int compare(SqlValue o1, SqlValue o2) {
                if(o1.getDataElement().getFieldClass().isPrimitive()){
                    if(o2.getDataElement().getFieldClass().isPrimitive())
                    {
                        return 0;
                    }else{
                        return -1;
                    }
                }else{
                    if(o2.getDataElement().getFieldClass().isPrimitive())
                    {
                        return 1;
                    }else{
                        return 0;
                    }
                }
            }});
        svs.addAll(svwhere);
        this.sqlvalues = svs .toArray(new SqlValue[svs.size()]);

    }

}
