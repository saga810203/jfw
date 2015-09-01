package org.jfw.core.code.generator.orm.impl;

public class ByteSet extends AbstractSetHandler {

    @Override
    protected String getMethodName4JDBCWrite() {
        return "setByte";
    }

    @Override
    protected int getJdbcType() {
        return java.sql.Types.TINYINT;
    }

}
