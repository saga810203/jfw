package org.jfw.core.code.generator.impl;

public class BooleanGet  extends AbstractGetHandler{

    @Override
    protected String getMethodName4JDBCRead() {
        return "getString";
    }

    @Override
    public void readValue(StringBuilder sb) {
        if (this.nullable && (!this.javaType.isPrimitive())) {
            String local = Utils.getLocalVarName();
            sb.append(this.javaType.getName()).append(" ").append(local).append(" =").append(this.el4Read).append(";")
                .append("if(").append(this.el4WasNull).append("){")
                .append(this.el4Write).append("(null);")
                .append("}else{")
                .append(this.el4Write).append("(\"1\".equals(").append(local).append("));}");
        } else {            
            sb.append(this.el4Write).append("((\"1\".equals(").append(this.el4Read)   .append("));");
        }
    }

}
