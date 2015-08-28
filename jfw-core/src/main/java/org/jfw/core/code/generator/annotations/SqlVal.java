package org.jfw.core.code.generator.annotations;

import org.jfw.core.code.generator.enums.DE;

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
