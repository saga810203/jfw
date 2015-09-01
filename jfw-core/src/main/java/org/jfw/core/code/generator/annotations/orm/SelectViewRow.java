package org.jfw.core.code.generator.annotations.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectViewRow {
    Class<?> beanClass() default Object.class;
    String filter() default "";
    String order() default "";
    SqlVal[] sqlVal() default {};
    /**
     * true and ? and ?            false  ? or ? or ?
     * @return
     */
    boolean and() default true;
    boolean dynamicFilter() default false;  
}
