package org.jfw.core.code.generator.impl;

public class ShortSet  extends AbstractSetHandler{

    @Override
    protected String getMethodName4JDBCWrite() {
        return "setShort";
    }

    @Override
    protected int getJdbcType() {
        return java.sql.Types.SMALLINT;
    }

}
