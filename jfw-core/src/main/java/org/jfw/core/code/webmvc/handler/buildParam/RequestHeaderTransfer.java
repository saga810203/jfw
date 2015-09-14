package org.jfw.core.code.webmvc.handler.buildParam;

import java.lang.reflect.Type;

import org.jfw.core.code.generator.annotations.webmvc.RequestHeader;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;

public interface RequestHeaderTransfer {
    void transfer(StringBuilder sb,int paramIndex,Type type,ControllerMethodCodeGenerator cmcg,RequestHeader annotation);
    void transferBeanProperty(StringBuilder sb,int paramIndex,Type type,ControllerMethodCodeGenerator cmcg,RequestHeaderTransfer.FieldRequestParam frp);
    
    public static class FieldRequestParam{
        private String value;
        private String paramName;
        private Class<?> clazz;
        private String defaultValue;
        private boolean required;
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public String getParamName() {
            return paramName;
        }
        public void setParamName(String paramName) {
            this.paramName = paramName;
        }
        public Class<?> getClazz() {
            return clazz;
        }
        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }
        public String getDefaultValue() {
            return defaultValue;
        }
        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }
        public boolean isRequired() {
            return required;
        }
        public void setRequired(boolean required) {
            this.required = required;
        }
    }
}
