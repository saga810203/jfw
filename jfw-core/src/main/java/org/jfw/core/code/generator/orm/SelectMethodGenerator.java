package org.jfw.core.code.generator.orm;

import org.jfw.core.code.generator.annotations.handler.orm.SelectField;

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
								+ this.parentType.getName() + "."
								+ this.method.getName());
			}
		}
	}

	protected void buildQuerySQLWithDynamic(StringBuilder sb) {
		sb.append("StringBuilder sql = new StringBuilder();")
				.append(" sql.append(\"").append(this.sql).append(" WHERE ");
		if (this.and) {
			sb.append("1=1");
		} else {
			sb.append("1=2");
		}
		sb.append("\";");

		for (int i = 0; i < sqlVals.length; ++i) {
			if (this.sqlVals[i].dataElement().getFieldClass().isPrimitive()) {
				sb.append("sql.append(\"").append(this.getAndOr())
						.append(this.sqlVals[i].sqlEl()).append("\");");
			} else {
				this.psshs[i].codeBeginCheckInSetOrWhere(sb);
				sb.append("sql.append(\"").append(this.getAndOr())
						.append(this.sqlVals[i].sqlEl()).append("\");");
				this.psshs[i].codeEndCheckInSetOrWhere(sb);
			}
		}
		if (null != this.order && this.order.length() > 0) {
			sb.append(" ORDER BY ").append(this.order);
		}
		sb.append("\";");
	}

	@Override
	protected void buildQuerySQL(StringBuilder sb) {
		this.checkSqlVals();
		this.dynamicSql = this.dynamicFilter && sqlVals.length > 0
				&& (this.filter.length() == 0);
		if (this.dynamicSql) {
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
		for(int i = 0 ; i < this.psshs.length ; ++i)
		{
		    if(this.psshs[i].isReplaceResource()) this.psshs[i].replaceResource(sb);
		}
		sb.append("try{");
		if (!this.singleRow) {
			sb.append("java.util.List<").append(this.classname4ReturnType)
					.append("> result = new java.util.LinkedList<")
					.append(this.classname4ReturnType).append(">();")
					.append("while(rs.next()){");
		} else {
			sb.append("if(rs.next()){");
		}
		if (this.needNew4ReturnType) {
			sb.append(this.classname4ReturnType).append(" obj = new ")
					.append(this.classname4ReturnType).append("();");
		}
		for (int i = 0; i < this.fields.length; ++i) {
			ResultSetGetHandler rsgh = null;
			try {
				rsgh = this.fields[i].getGetClass().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			rsgh.init(i + 1, null, this.fields[i].isNullable(),
					this.fields[i].getBeanFieldName(),
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
}
