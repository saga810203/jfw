package org.jfw.core.code.generator.annotations.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface View {
    String value();
    InheritedTable inherited() default @InheritedTable(Object.class);    
}
