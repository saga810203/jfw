package org.jfw.core.code.generator.impl;

public class IntGet extends AbstractGetHandler {

	@Override
	protected String getMethodName4JDBCRead() {
		return "getInt";
	}
}
