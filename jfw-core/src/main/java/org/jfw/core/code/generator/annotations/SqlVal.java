package org.jfw.core.code.generator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.generator.enums.DE;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SqlVal {
	/**
	 * 赋值时使用，指定参数据名称  参数按顺序为 con,param1,param2,param3,param4,param4..........
	 * @return
	 */
	int paramIndex() default 1;

	/**
	 * 与paramIndex一起用生成取sql参数值的方式。默认值 只取param+paramIndex作为参数;
	 * 以点开始<如：.getId()>直接合成;
	 * 其它生成JavaBean的getter; 如：id ==> .getId()
	 * @return
	 */
	String field() default "";
	
	/**
	 * sql表达式，如：  a.ID >?      name=?   等
	 * 在动态生成动态Where时用
	 */
	String sqlEl() default "";
	
	DE dataElement();
}
