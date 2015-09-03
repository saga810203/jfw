package org.jfw.core.code.generator.orm;

import java.util.Arrays;
import java.util.Comparator;

import org.jfw.core.code.generator.annotations.handler.orm.SelectField;
import org.jfw.core.code.generator.annotations.orm.SqlVal;

public abstract class SelectMethodGenerator extends JDBCMethodGenerator {
    protected String sql;
    protected String filter;
    protected String order;
    protected boolean and;
    protected boolean dynamicFilter;
    protected SelectField[] fields;
    protected boolean singleRow = false;
    protected String classname4ReturnType;
    protected boolean needNew4ReturnType;

    private String getAndOr() {
        return this.and ? " AND " : " OR ";
    }

    private void checkSqlVals() {
        if ((this.filter.length() > 0) || (this.sqlVals.length == 0))
            return;
        for (int i = 0; i < this.sqlVals.length; ++i) {
            if (this.sqlVals[i].sqlEl().trim().length() == 0) {
                throw new IllegalArgumentException(
                        "invalid annotation value sqlVal (reason: sqlEl is empyt) in Mehtod:"
                                + this.parentType.getName() + "." + this.method.getName());
            }
        }
    }

    protected void buildQuerySQLWithDynamic(StringBuilder sb) {
        sb.append("StringBuilder sql = new StringBuilder();")
           .append(" sql.append(\"").append(this.sql).append("\");");
        if (this.sqlVals.length == 1) {
                this.psshs[0].codeBeginCheckInSetOrWhere(sb);
                   sb.append("sql.append(\" WHERE ").append(this.sqlVals[0].sqlEl()).append("\");");
                this.psshs[0].codeEndCheckInSetOrWhere(sb);
        } else {
           // sb.append("StringBuilder sqlwhere = new StringBuilder();");
            if(this.sqlVals[0].dataElement().getFieldClass().isPrimitive())
            {
                int beginIndex = 0;
                sb.append("sql.append(\" WHERE ");
                for(int i =0 ; i < this.sqlVals.length ; ++i)
                {
                    if(!this.sqlVals[i].dataElement().getFieldClass().isPrimitive()){
                        beginIndex = i ;
                        break;
                    }
                    if(i!=0)sb.append(this.getAndOr());
                    sb.append(this.sqlVals[i]);
                }
                sb.append("\");");
                for (int i = beginIndex; i < sqlVals.length; ++i) {
                    this.psshs[i].codeBeginCheckInSetOrWhere(sb);
                    sb.append("sql.append(\"").append(this.getAndOr()).append(this.sqlVals[i].sqlEl()).append("\");");
                    this.psshs[i].codeEndCheckInSetOrWhere(sb);
                }
            }else{
                sb.append("StringBuilder sqlw = new StringBuilder();");
                for (int i = 0; i < sqlVals.length; ++i) {
                    this.psshs[i].codeBeginCheckInSetOrWhere(sb);
                    sb.append("sqlw.append(\"").append(this.getAndOr()).append(this.sqlVals[i].sqlEl()).append("\");");
                    this.psshs[i].codeEndCheckInSetOrWhere(sb);
                }
                sb.append("if(sqlw.length()>0){sql.append(\" WHERE \").append(sqlw.toString().substring(")
                .append(this.and?5:4).append("));}");
            }  
        }
        if (null != this.order && this.order.length() > 0) {
            sb.append("sql.append(\"").append(" ORDER BY ").append(this.order).append("\");");
        }
    }

    protected boolean isAllPrimitiveParamter()
    {
        for(SqlVal sv:this.sqlVals)
        {
            if(!sv.dataElement().getFieldClass().isPrimitive()) return false;
        }
        return true;
    }
    @Override
    protected void buildSQL(StringBuilder sb) {
        this.checkSqlVals();
        this.dynamicSql = this.dynamicFilter && sqlVals.length > 0 && (this.filter.length() == 0);
        if (this.dynamicSql && (!this.isAllPrimitiveParamter())) {
            this.buildQuerySQLWithDynamic(sb);
            return;
        }
        sb.append("String sql = \"").append(this.sql);
        if (filter.length() > 0) {
            sb.append(" WHERE ").append(this.filter);
        } else if (sqlVals.length > 0) {
            sb.append(" WHERE ");
            for (int i = 0; i < sqlVals.length; ++i) {
                if (i != 0) {
                    if (this.and) {
                        sb.append(" AND ");
                    } else {
                        sb.append(" OR ");
                    }
                }
                sb.append(sqlVals[i].sqlEl());
            }
        }
        if (null != this.order && this.order.length() > 0) {
            sb.append(" ORDER BY ").append(this.order);
        }
        sb.append("\";");
    }

    @Override
    protected void buildHandleResult(StringBuilder sb) {
        sb.append("java.sql.ResultSet rs = ps.executeQuery();");
        for (int i = 0; i < this.psshs.length; ++i) {
            if (this.psshs[i].isReplaceResource())
                this.psshs[i].replaceResource(sb);
        }
        sb.append("try{");
        if (!this.singleRow) {
            sb.append("java.util.List<").append(this.classname4ReturnType)
                    .append("> result = new java.util.LinkedList<").append(this.classname4ReturnType).append(">();")
                    .append("while(rs.next()){");
        } else {
            sb.append("if(rs.next()){");
        }
        if (this.needNew4ReturnType) {
            sb.append(this.classname4ReturnType).append(" obj = new ").append(this.classname4ReturnType).append("();");
        }
        for (int i = 0; i < this.fields.length; ++i) {
            ResultSetGetHandler rsgh = null;
            try {
                rsgh = this.fields[i].getGetClass().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            rsgh.init(i + 1, null, this.fields[i].isNullable(), this.fields[i].getBeanFieldName(),
                    this.fields[i].getFieldClass());
            rsgh.readValue(sb);
        }
        if (!this.singleRow) {
            sb.append("result.add(obj);}return result;");
        } else {
            sb.append("return obj;}return null;");
        }
        sb.append("}finaly{try{rs.close();}catch(SQLException e){}}");
    }

    protected void buildQueryParamter(StringBuilder sb) {
        if (this.psshs.length > 0) {
            sb.append("int paramIndex = 1;");
            if (this.dynamicSql) {
                for (int i = 0; i < this.psshs.length; ++i) {
                    this.psshs[i].wirteValueWithCheck(sb);
                }
            } else {
                for (int i = 0; i < this.psshs.length; ++i) {
                    this.psshs[i].wirteNotNullValue(sb);
                }
            }
        }
    }

    protected void sortSqlVals() {

        if (this.sqlVals.length < 2)
            return;
        Arrays.sort(this.sqlVals, new Comparator<SqlVal>() {

            @Override
            public int compare(SqlVal o1, SqlVal o2) {
                if (o1.dataElement().getFieldClass().isPrimitive()) {
                    if (o2.dataElement().getFieldClass().isPrimitive()) {
                        return 0;
                    } else {
                        return -1;
                    }

                } else {
                    if (o2.dataElement().getFieldClass().isPrimitive()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });
    }
}
