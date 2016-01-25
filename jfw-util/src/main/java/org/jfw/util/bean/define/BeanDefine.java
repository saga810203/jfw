package org.jfw.util.bean.define;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jfw.util.bean.BeanBuilder;
import org.jfw.util.bean.BeanFactory;

public abstract class BeanDefine {
	private BeanFactory factory;    	
	private boolean singleton = true;
	private String key;
	private String name;
	private String value;
	private List<String> depends = new ArrayList<String>();	
	
	protected BeanDefine(BeanFactory bf,String key,String value){
		this.factory = bf;
		this.key = key;
		this.value = value;
		this.name= key;
		int i = name.indexOf("::");
		if(i>0)this.name = name.substring(0, i);
		this.singleton = key.indexOf("::prototype")>0;
	}
	
	public void addAttributes(BeanFactory bf,Map<String,String> attrs){
		for(Map.Entry<String,String> entry:attrs.entrySet()){
			this.addAttributeEntry(bf,entry.getKey(),entry.getValue());
		}
	}
	public void addAttributeEntry(BeanFactory bf,String vkey,String vVal){
		if(vkey.startsWith(this.name+".")){
			this.addAttributeInternal(bf,vkey.substring(this.name.length()+1),vVal);
		}
	}
	public abstract void addAttributeInternal(BeanFactory bf,String attrName,String attrVal);	
	
	
	public abstract Object buildBean(BeanFactory bf);
	public abstract BeanBuilder buildBeanBuilder(BeanFactory bf);
	
	public boolean addDependBean(String beanid){
		if(!this.factory.contains(beanid)) return false;
		if(!this.depends.contains(beanid)){
			this.depends.add(beanid);
		}
		return true;
	}
	public List<String> getDependBeans(){
		return Collections.unmodifiableList(this.depends);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	} 
}
