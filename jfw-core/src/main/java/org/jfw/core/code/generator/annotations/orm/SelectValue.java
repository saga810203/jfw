package org.jfw.core.code.generator.annotations.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.generator.annotations.handler.orm.SelectValueMG;
import org.jfw.core.code.generator.enums.orm.DE;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectValue {
    DE  resultType();
    String sql();
	String filter() default "";
	SqlVal[] sqlVal() default {};
//	boolean dynamicWhere() default false;
	/**
	 * true �� and ? and ?            false  ? or ? or ?
	 * @return
	 */
	boolean and() default true;
	boolean dynamicFilter() default false;	
    boolean bulidMehtod() default true;
    Class<? extends MethodCodeGenerator> buildHandleClass() default SelectValueMG.class;
}
