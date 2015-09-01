package org.jfw.core.code.generator.orm.impl;

public class DoubleGet  extends AbstractGetHandler{

    @Override
    protected String getMethodName4JDBCRead() {
        return "getDouble";
    }
    
}
