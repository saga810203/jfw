package org.jfw.core.code.generator.orm.impl;

public class FloatGet extends AbstractGetHandler {

    @Override
    protected String getMethodName4JDBCRead() {
        return "getFloat";
    }

}
