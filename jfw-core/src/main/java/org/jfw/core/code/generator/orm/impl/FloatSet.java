package org.jfw.core.code.generator.orm.impl;

public class FloatSet extends AbstractSetHandler{

    @Override
    protected String getMethodName4JDBCWrite() {
        return "setFloat";
    }

    @Override
    protected int getJdbcType() {
        return java.sql.Types.FLOAT;
    }

}
