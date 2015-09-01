package org.jfw.core.code.generator.orm.impl;

public class IntGet extends AbstractGetHandler {

	@Override
	protected String getMethodName4JDBCRead() {
		return "getInt";
	}
}
