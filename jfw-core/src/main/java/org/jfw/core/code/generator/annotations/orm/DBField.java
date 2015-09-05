package org.jfw.core.code.generator.annotations.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.generator.enums.orm.DE;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBField {
	/**
	 * �ֶ�����SQL�����е�Select ����
	 * @return
	 */
	String name() default "" ;
	/**
	 * �ֶα�����SQL�����е�Select ����
	 * һ����VO��ʹ��
	 * @return
	 */
	String alias() default "";
	/*
	 *�Ƿ��Ǽ����ֶ�
	 */
	boolean calcField() default false;
	DE value();
   /**
    * �Ƿ���DE value������ nullable�෴
    * @return
    */
	boolean xorNullable() default false;
	boolean xorCanFilter() default false;
	boolean xorInSelect() default false;
	boolean xorInUpdate() default false;
	/*
	 * �Ƿ�������
	 */
	boolean primaryKey() default false;
}
