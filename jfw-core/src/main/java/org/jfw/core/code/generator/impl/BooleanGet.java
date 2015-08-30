package org.jfw.core.code.generator.impl;

public class BooleanGet extends AbstractGetHandler {

    @Override
    protected String getMethodName4JDBCRead() {
        return "getString";
    }

    @Override
    public void readValue(StringBuilder sb) {
        if (null != this.el4Write) {
            if (this.nullable && (!this.javaType.isPrimitive())) {
                String local = Utils.getLocalVarName();
                sb.append(this.javaType.getName()).append(" ").append(local).append(" =\"1\".equals(").append(this.el4Read)
                        .append(");").append("if(").append(this.el4WasNull).append("){").append(this.el4Write)
                        .append("(null);").append("}else{").append(this.el4Write).append("(")
                        .append(local).append(");}");
            } else {
                sb.append(this.el4Write).append("((\"1\".equals(").append(this.el4Read).append("));");
            }
        } else {
            if (this.nullable && (!this.javaType.isPrimitive())) {
                sb.append(this.javaType.getName()).append(" obj =\"1\".equals(").append(this.el4Read)
                        .append(");")
                .append("if(").append(this.el4WasNull).append("){obj=null;}");
            } else {
                sb.append(this.javaType.getName()).append(" obj = \"1\".equals(").append(this.el4Read).append(");");
            }
        }
    }

}
