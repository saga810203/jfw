package org.jfw.util.bean.define;

import java.util.ArrayList;
import java.util.List;

import org.jfw.util.bean.BeanBuilder;
import org.jfw.util.bean.BeanFactory;

public class ClassBeanDefine extends BeanDefine {
	private Class<?> clazz;

	private List<ValueDefine> attrs = new ArrayList<ValueDefine>();

	public static BeanDefine build(String key, String val, BeanFactory bf) {
		ClassBeanDefine cbf = new ClassBeanDefine(bf, key, val);
		try {
			cbf.clazz = Class.forName(val.trim());
		} catch (Exception e) {
			throw new RuntimeException("create bean with[" + key + "=" + val + "]", e);
		}
		return cbf;
	}

	protected ClassBeanDefine(BeanFactory bf, String key, String value) {
		super(bf, key, value);
	}

	@Override
	public Object buildBean(BeanFactory bf) {
		return this.buildBeanBuilder(bf).build(bf);
	}

	@Override
	public BeanBuilder buildBeanBuilder(BeanFactory bf) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAttributeInternal(BeanFactory bf, String name, String attrVal) {
		String className = null;
		String attrName = name;
		int index = attrName.indexOf("::");
		if(index >0) {
			className = attrName.substring(index+2);
		}else{
			attrName = attrName.substring(0,index);
		}
		boolean isRef = attrName.endsWith("-ref");
		if(isRef){
			attrName  = attrName.substring(0,attrName.length()-4);
		}
		for(ValueDefine vd:this.attrs){
			if(vd.getName().equals(attrName)) throw new RuntimeException("duplicate bean attribute["+attrName+"]");
		}
		
		Class<?> clazz = null;
		if(className!=null&&className.trim().length()>0){
			clazz = ValueDefine.converToValueClass(className.trim());
		}
		ValueDefine vd = ValueDefine.build(bf, attrName, clazz, isRef, attrVal);
		if(isRef) this.addDependBean(attrVal);
		this.attrs.add(vd);
	}
}
