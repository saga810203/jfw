package org.jfw.core.code.generator.orm.impl;

public class LongSet  extends AbstractSetHandler{

    @Override
    protected String getMethodName4JDBCWrite() {
        return "setLong";
    }

    @Override
    protected int getJdbcType() {
        return java.sql.Types.BIGINT;
    }

}
