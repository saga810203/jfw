package org.jfw.core.code.generator.annotations.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.generator.annotations.handler.orm.UpdateSqlMG;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateSql {
	String value();
	SqlVal[] sqlvalue() default {};
	SqlVal[] where() default{};
    boolean bulidMehtod() default true;
    Class<? extends MethodCodeGenerator> buildHandleClass() default UpdateSqlMG.class;
}
