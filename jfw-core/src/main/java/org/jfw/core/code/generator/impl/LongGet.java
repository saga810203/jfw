package org.jfw.core.code.generator.impl;

public class LongGet  extends AbstractGetHandler{

    @Override
    protected String getMethodName4JDBCRead() {
        return "getLong";
    }

}
