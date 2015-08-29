package org.jfw.core.code.generator.impl;

public class DoubleGet  extends AbstractGetHandler{

    @Override
    protected String getMethodName4JDBCRead() {
        return "getDouble";
    }
    
}
