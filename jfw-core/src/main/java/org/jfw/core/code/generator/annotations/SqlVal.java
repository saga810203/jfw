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
	 * ��ֵʱʹ�ã�ָ������������  ������˳��Ϊ con,param1,param2,param3,param4,param4..........
	 * @return
	 */
	String beanName() default "param1";
	/**
	 * ��beanNameһ��������ȡsql����ֵ�ķ�ʽ��Ĭ��ֵ ֻȡbeanName��Ϊ����;
	 * �Ե㿪ʼ<�磺.getId()>ֱ�Ӻϳ�;
	 * ��������JavaBean��getter; �磺id ==> .getId()
	 * @return
	 */
	String field() default "";
	
	/**
	 * sql���ʽ���磺  a.ID >?      name=?   ��
	 * �ڶ�̬���ɶ�̬Whereʱ��
	 */
	String sqlEl() default "";
	
	DE dataElement();
}
