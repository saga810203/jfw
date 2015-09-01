package org.jfw.core.code.generator.orm.impl;


public class StringGet extends AbstractGetHandler {
	@Override
	protected String getMethodName4JDBCRead() {
		return "getString";
	}
}
