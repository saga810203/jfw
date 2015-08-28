package org.jfw.core.code.generator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.generator.enums.DE;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBField {
	/**
	 * 字段名，在SQL语名中的Select 部分
	 * @return
	 */
	String name() ;
	/**
	 * 字段别名，在SQL语名中的Select 部分
	 * 一般在VO中使用
	 * @return
	 */
	String alias() default "";
	/*
	 *是否是计算字段
	 */
	boolean calcField() default false;
	DE value();
   /**
    * 是否与DE value声明的 nullable相反
    * @return
    */
	boolean xorNullable() default false;
	boolean xorCanFilter() default false;
	boolean xorInSelect() default false;
	boolean xorInUpdate() default false;
	/*
	 * 是否是主键
	 */
	boolean primaryKey() default false;
}
