package org.jfw.core.code.generator.impl;

public class BigDecimalGet extends AbstractGetHandler {

    @Override
    protected String getMethodName4JDBCRead() {
        return "getBigDecimal";
    }

}
