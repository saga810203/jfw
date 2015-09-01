package org.jfw.core.code.generator.orm.impl;

public class ByteGet extends AbstractGetHandler {

    @Override
    protected String getMethodName4JDBCRead() {
        return "getByte";
    }

}
