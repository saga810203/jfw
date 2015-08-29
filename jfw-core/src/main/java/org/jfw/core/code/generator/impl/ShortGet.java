package org.jfw.core.code.generator.impl;

public class ShortGet  extends AbstractGetHandler{

    @Override
    protected String getMethodName4JDBCRead() {
        return "getShort";
    }

}
