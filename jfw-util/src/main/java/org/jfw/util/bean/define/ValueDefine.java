package org.jfw.util.bean.define;

import org.jfw.util.bean.BeanFactory;

public abstract class ValueDefine implements Comparable<ValueDefine> {
	protected String name;
	protected Class<?> clazz;

	public String getName(){
		return name;
	}	
	@Override
	public int compareTo(ValueDefine o) {
		return this.name.compareTo(o.name);
	}
	public Class<?> getValueClass(){
		return clazz;
	}
	
	public abstract void init(BeanFactory bf,String name,Class<?> clazz,boolean isRef,String val) throws ConfigException;
	public abstract Object getValue(BeanFactory bf);
	
	public static Class<?> converToValueClass(String cls){
		if(cls.equals("int") || cls.equals("java.lang.Integer")){
			return java.lang.Integer.class;
		} else if(cls.equals("byte") || cls.equals("java.lang.Byte")){
			return java.lang.Byte.class;
		} else if(cls.equals("short") || cls.equals("java.lang.Short")){
			return java.lang.Short.class;
		} else if(cls.equals("long") || cls.equals("java.lang.Long")){
			return java.lang.Long.class;
		} else if(cls.equals("float") || cls.equals("java.lang.Float")){
			return java.lang.Float.class;
		} else if(cls.equals("double") || cls.equals("java.lang.Double")){
			return java.lang.Integer.class;
		} else if(cls.equals("boolean") || cls.equals("java.lang.Boolean")){
			return java.lang.Boolean.class;
		} else if(cls.equals("char") || cls.equals("java.lang.Character")){
			return java.lang.Character.class;
		} else if(cls.equals("java.lang.String")){
			return java.lang.String.class;
		} else if(cls.equals("java.math.BigInteger")){
			return java.math.BigInteger.class;
		} else if(cls.equals("java.math.BigDecimal")){
			return java.math.BigDecimal.class;
		} else if(cls.equals("java.lang.Class")){
			return java.lang.Class.class;
		} 
		throw new RuntimeException("UnsupportedClass["+cls+"] with fixvalue in BeanFactory cofig");
	}
	
	public static ValueDefine build(BeanFactory bf,String name,Class<?> clazz,boolean isRef,String val) throws ConfigException{
		ValueDefine vd = null;
		if(isRef){
			if(!bf.contains(val))throw new ConfigException("notfound bean["+val+"]");
			if(bf.isSingletonBean(val)){
				vd = new RefSingleBeanVD();
			}else{
				vd = new RefPrototypeBeanVD();
			}
		}
		vd = new FixedValueDefine();
		vd.init(bf, name, clazz, isRef, val);
		return vd;
		
	}
	
}
