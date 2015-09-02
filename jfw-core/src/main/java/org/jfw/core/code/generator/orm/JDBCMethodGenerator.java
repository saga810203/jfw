package org.jfw.core.code.generator.orm;

import java.lang.reflect.Type;

import org.jfw.core.code.generator.annotations.orm.SqlVal;
import org.jfw.core.code.generator.enums.orm.DE;
import org.jfw.core.code.orm.MethodGenerator;

public abstract class JDBCMethodGenerator extends MethodGenerator {
	protected boolean dynamicSql = false;
	protected SqlVal[] sqlVals = new SqlVal[0];
	protected PreparedStatementSetHandler[] psshs = new PreparedStatementSetHandler[0];
	protected SqlValue[] sqlvalues = new SqlValue[0];

	private void createSqlVaues() {
		if ((this.sqlvalues.length > 0) || (0 == this.sqlVals.length))
			return;
		this.sqlvalues = new SqlValue[this.sqlVals.length];
		for (int i = 0; i < this.sqlVals.length; ++i) {
			this.sqlvalues[i] = new SqlValue(this.sqlVals[i]);
		}
	}

	private void initPreparedStatementSetHander(PreparedStatementSetHandler pssh, SqlValue sv) {
       String beanName = "param"+sv.paramIndex;
		if (sv.field.startsWith(".")) {
			pssh.init(beanName, null, beanName+sv.field, sv.getDataElement().getFieldClass());
		} else {
			pssh.init(beanName, sv.field, null, sv.getDataElement().getFieldClass());
		}
	}

	protected void buildPSSH() {
		this.createSqlVaues();
		if (this.sqlvalues.length > 0) {
			this.psshs = new PreparedStatementSetHandler[this.sqlvalues.length];
			for (int i = 0; i < this.sqlvalues.length; ++i) {
				try {
					this.psshs[i] = this.sqlvalues[i].getDataElement().getWriteClass().newInstance();
					this.initPreparedStatementSetHander(psshs[i], this.sqlvalues[i]);
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
		if (this.dynamicSql) {
			sb.append(".toString()");
		}
		sb.append(");");
		sb.append("try{");
		this.buildQueryParamter(sb);
		this.buildHandleResult(sb);
		sb.append("}finally{try{ps.close();}catch(SQLException e){}}");
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
	protected void buildParameters(StringBuilder sb){
	    int index=1;
	    Type[] ts = this.method.getGenericParameterTypes();
	    
	    for(int i = 0 ; i < ts.length ; ++i)
	    {
	        if (i!= 0 ) sb.append(",");
	        writeNameOfType(ts[i], sb);
	        sb.append(" ");
	        if (java.sql.Connection.class==ts[i]){
	            if(i!=0 ) throw new IllegalArgumentException("java.sql.Connection must at index 0 in method");
	            sb.append("con");
	        }else{
	            sb.append("param").append(index++);
	        }
	    }
	}
	protected abstract void buildQuerySQL(StringBuilder sb);
	protected abstract void buildHandleResult(StringBuilder sb);

	public static class SqlValue {
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

		public SqlValue(SqlVal sv) {
			this.paramIndex = sv.paramIndex();
			this.field = sv.field().trim();
			this.dataElement = sv.dataElement();
			this.sqlEl = sv.sqlEl().trim();
		}

		public SqlValue(int paramIndex, String field, String sqlEl, DE dataElement) {
			this.paramIndex = paramIndex;
			this.field = field.trim();
			this.sqlEl = sqlEl.trim();
			this.dataElement = dataElement;
		}
	}
}
