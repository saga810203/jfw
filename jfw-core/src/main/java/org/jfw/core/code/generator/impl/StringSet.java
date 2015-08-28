package org.jfw.core.code.generator.impl;


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
