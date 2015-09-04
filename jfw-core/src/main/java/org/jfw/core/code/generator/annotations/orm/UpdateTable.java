package org.jfw.core.code.generator.annotations.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.generator.annotations.handler.orm.UpdateTableMG;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateTable {
	Class<?> value() default Object.class;
	boolean dynamic() default false;
	boolean bulidMehtod() default true;
	Class<? extends MethodCodeGenerator> buildHandleClass() default UpdateTableMG.class;
}
