package org.jfw.core.code.generator.annotations.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.generator.annotations.handler.orm.InsertTableMG;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InsertTable {
	Class<?> value() default Object.class;
	boolean bulidMehtod() default true;
	Class<?> buildHandleClass() default InsertTableMG.class;
}

