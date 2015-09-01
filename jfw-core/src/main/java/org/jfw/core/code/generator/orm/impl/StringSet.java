package org.jfw.core.code.generator.orm.impl;


public class StringSet extends AbstractSetHandler {
	@Override
	protected String getMethodName4JDBCWrite() {
		return "setString";
	}

	@Override
	protected int getJdbcType() {
		return java.sql.Types.VARCHAR;
	}
	
}
