package org.jfw.core.code.generator.impl;

public class BigDecimalSet  extends AbstractSetHandler{

    @Override
    protected String getMethodName4JDBCWrite() {
        return "setBigDecimal";
    }

    @Override
    protected int getJdbcType() {
        return java.sql.Types.NUMERIC;
    }

}
