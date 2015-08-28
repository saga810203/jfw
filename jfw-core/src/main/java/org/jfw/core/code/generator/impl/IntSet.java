package org.jfw.core.code.generator.impl;

public class IntSet  extends AbstractSetHandler {

	@Override
	protected String getMethodName4JDBCWrite() {
		return "setInt";
	}

	@Override
	protected int getJdbcType() {
		return java.sql.Types.INTEGER;
	}

}
