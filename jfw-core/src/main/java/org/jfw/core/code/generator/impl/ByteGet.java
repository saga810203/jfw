package org.jfw.core.code.generator.impl;

public class ByteGet extends AbstractGetHandler {

    @Override
    protected String getMethodName4JDBCRead() {
        return "getByte";
    }

}
