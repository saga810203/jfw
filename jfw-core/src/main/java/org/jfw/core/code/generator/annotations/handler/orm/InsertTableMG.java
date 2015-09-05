package org.jfw.core.code.generator.annotations.handler.orm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jfw.core.code.generator.annotations.orm.DBField;
import org.jfw.core.code.generator.annotations.orm.InsertTable;
import org.jfw.core.code.generator.orm.UpdateMethodGenerator;

public class InsertTableMG extends UpdateMethodGenerator {
    private InsertTable it;
    private String tableName;
    protected Field[] fields;
    private Map<String, String> fixSqlValue = new HashMap<String, String>();

    @Override
    protected void buildSQL(StringBuilder sb) {
        StringBuilder val = new StringBuilder();
        sb.append("String sql=\"INSERT INTO ").append(this.tableName).append(" (");
        boolean addCama = false;
        for (Map.Entry<String, String> entry : fixSqlValue.entrySet()) {
            if (addCama) {
                sb.append(",");
                val.append(",");
            }
            addCama = true;
            sb.append(entry.getKey());
            val.append(entry.getValue());
        }
        for (int i = 0; i < this.sqlvalues.length; ++i) {
            if (addCama) {
                sb.append(",");
                val.append(",");
            }
            addCama = true;
            sb.append(this.sqlvalues[i].getSqlEl());
            val.append("?");
        }
        sb.append(") VALUES (").append(val.toString()).append(")\";");
    }

    private Class<?> getTableClassFromParameters() {
        Class<?>[] cls = this.method.getParameterTypes();
        if (cls.length < 1 || cls.length > 2)
            throw new RuntimeException("invalid args with annotation @InsertTable at method:"
                    + this.parentType.getName() + "." + this.method.getName());
        if (cls.length == 1)
            return cls[0];
        if (cls[0] == java.sql.Connection.class)
            return cls[1];
        if (cls[1] != java.sql.Connection.class)
            throw new RuntimeException("invalid args with annotation @InsertTable at method:"
                    + this.parentType.getName() + "." + this.method.getName());
        return cls[0];
    }

    @Override
    public void aferInit() {
        this.dynamicSql = false;
        this.checkReturnType();
        this.it = this.method.getAnnotation(InsertTable.class);
        if (null == it)
            throw new RuntimeException("not found @InsertTable at method: " + this.parentType.getName() + "."
                    + this.method.getName());
        Class<?> anCls = it.value();
        Class<?> paCls = getTableClassFromParameters();
        if (anCls == Object.class) {
            anCls = paCls;
        }
        if (!anCls.isAssignableFrom(paCls)) {
            throw new RuntimeException("invalid args or @InsertTable.value: " + this.parentType.getName() + "."
                    + this.method.getName());
        }
        this.tableName = POUtil.GetTableName(anCls);
        this.fields = POUtil.getInsertFieldInTable(anCls).toArray(new Field[0]);
        LinkedList<SqlValue> svs = new LinkedList<SqlValue>();
        for (int i = 0; i < this.fields.length; ++i) {
            DBField df = this.fields[i].getAnnotation(DBField.class);
            String columnName = df.name();
            columnName=null==columnName?"":columnName.trim();
            if(columnName.length()==0) columnName =POUtil.fieldName2ColumnName(this.fields[i].getName());
            
            String iv = df.value().getDefaultSqlValueForInsert();
            if (null != iv && (iv.trim().length() > 0)) {
                this.fixSqlValue.put(columnName, iv.trim());
            } else {
                svs.add(new SqlValue(1, this.fields[i].getName(), columnName, df.value()));
            }
        }
        this.sqlvalues = svs.toArray(new SqlValue[svs.size()]);
    }

    @Override
    protected void buildQueryParamter(StringBuilder sb) {
        if (this.psshs.length > 0) {
            sb.append("int paramIndex = 1;");
            for (int i = 0; i < this.psshs.length; ++i) {
                if(this.psshs[i].isNullable()){
                this.psshs[i].wirteValue(sb);
                }else{
                    this.psshs[i].wirteNotNullValue(sb);
                }
            }
        }

    }

}
