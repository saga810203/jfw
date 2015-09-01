package org.jfw.core.code.generator.orm.impl;

public class DoubleSet  extends AbstractSetHandler{

    @Override
    protected String getMethodName4JDBCWrite() {
        return "setDouble";
    }

    @Override
    protected int getJdbcType() {
        return java.sql.Types.DOUBLE;
    }

}
