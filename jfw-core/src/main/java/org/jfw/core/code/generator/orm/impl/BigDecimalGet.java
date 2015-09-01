package org.jfw.core.code.generator.orm.impl;

public class BigDecimalGet extends AbstractGetHandler {

    @Override
    protected String getMethodName4JDBCRead() {
        return "getBigDecimal";
    }

}
