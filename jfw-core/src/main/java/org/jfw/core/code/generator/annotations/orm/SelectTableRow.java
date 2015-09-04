package org.jfw.core.code.generator.annotations.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.generator.annotations.handler.orm.SelectTableRowMG;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectTableRow {
    Class<?> beanClass() default Object.class;
    String filter() default "";
    SqlVal[] sqlVal() default {};
    /**
     * true and ? and ?            false  ? or ? or ?
     * @return
     */
    boolean and() default true;
    boolean dynamicFilter() default false;  
    boolean bulidMehtod() default true;
    Class<? extends MethodCodeGenerator> buildHandleClass() default SelectTableRowMG.class;
}
