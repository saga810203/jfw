package org.jfw.core.code.generator.impl;


public class StringGet extends AbstractGetHandler {
	@Override
	protected String getMethodName4JDBCRead() {
		return "getString";
	}
}
