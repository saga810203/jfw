package org.jfw.core.code;

import java.lang.reflect.Method;

public interface MethodCodeGenerator {
    void init(Class<?> parentType,Method method,CodeGenerator cg);
    String build();
}
