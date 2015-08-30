package org.jfw.core.code.generator.annotations.handler;

import java.lang.reflect.Field;

import org.jfw.core.code.generator.ResultSetGetHandler;
import org.jfw.core.code.generator.annotations.DBField;

public class SelectField {
    private String dbFieldName;
    private String dbFieldAlias;
    private String beanFieldName;
    private boolean nullable;
    private Class<?> fieldClass;
    private Class<? extends ResultSetGetHandler> getClass;
    
    private SelectField(){}  

    public static SelectField  build(Field f) {
        SelectField sf = new SelectField();
        DBField df = f.getAnnotation(DBField.class);
        sf.fieldClass = f.getType();
        if(sf.fieldClass != df.value().getFieldClass()) throw new IllegalArgumentException("invalid annotation '@DBField' at Field :"+f.getDeclaringClass().getName()+"."+f.getName());
        sf.beanFieldName = f.getName();
        sf.dbFieldName = df.name().trim();
        if (sf.dbFieldName.length() == 0) {
            sf.dbFieldName = POUtil.fieldName2ColumnName(sf.beanFieldName);
        }
        sf.dbFieldAlias = df.alias().trim();
        sf.nullable = df.value().isNullable();
        if (df.xorNullable()){
            sf.nullable = !sf.nullable;
        }
        sf.getClass = df.value().getReadClass();
        return sf;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public String getDbFieldAlias() {
        return dbFieldAlias;
    }

    public void setDbFieldAlias(String dbFieldAlias) {
        this.dbFieldAlias = dbFieldAlias;
    }

    public String getBeanFieldName() {
        return beanFieldName;
    }

    public void setBeanFieldName(String beanFieldName) {
        this.beanFieldName = beanFieldName;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(Class<?> fieldClass) {
        this.fieldClass = fieldClass;
    }

    public Class<? extends ResultSetGetHandler> getGetClass() {
        return getClass;
    }

    public void setGetClass(Class<? extends ResultSetGetHandler> getClass) {
        this.getClass = getClass;
    }

}
