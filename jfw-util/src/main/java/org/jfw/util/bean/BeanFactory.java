package org.jfw.util.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
/**
 * beanid=classname                    //singleto bean
 * beanid::prototype=classname         //prototype bean
 * 
 * beanid::build=classname             //create bean by static method   step 1
 * beanid.build-method=methodname     //create bean by static method   step 2
 * 
 * beanid::factory-ref=beanid          //create bean by bean   method   step 1
 * beanid.factory-method=methodname   //create bean by bean   method   step 2
 * 
 * beanid::map=classname                       //create map bean;  
 * beanid.map-key-{seq}[::classname]=value    //map bean key id
 * beanid.map-val-{seq}[::classname]=value    //map bean value id   
 * 
 * beanid.map-key-{seq}-ref = beanid          //map bean key id ref    
 * beanid.map-val-{seq}-ref = beanid          //map bean key id ref
 * 
 * beanid::collection=class                             //create collection bean
 * beanid.collection-ele[-{seq}][::classname]=value    //collection element
 * beanid.collection-eleRef[-{seq}]=beanid             //collection element
 * 
 * beanid.attributename[::classname]=value
 * beanid.attributename-ref=beanid
 * 
 * 
 * @author Saga_
 *
 */
public class BeanFactory {
	public Object getBean(String id){
		Object result = this.singleton.get(id);
		if(result==null){
			result = this.buildBean(id);
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	public <T> T getBean(String id,Class<T> clazz){
		Object result = this.getBean(id);
		if(null==result&&(null!=this.parent)) result = parent.getBean(id);
		if(null!=result && clazz.isAssignableFrom(result.getClass()))
		{
			return (T)result;
		}
		return null;
	}
	public boolean contains(String beanid){
		return this.names.contains(beanid)||(this.parent!=null && this.parent.contains(beanid));
	}
	public boolean isSingletonBean(String beanid){
		return this.names4Singleton.contains(beanid)||(null!=this.parent && this.parent.isSingletonBean(beanid));
	}
    public List<String> listBeanName(){
    	if(null==this.parent)
    	
    	return Collections.unmodifiableList(this.names);
    	else{
    		List<String> result = new ArrayList<String>();
    		result.addAll(this.names);
    		result.addAll(this.parent.listBeanName());
    		return Collections.unmodifiableList(result);
    	}
    }
    
    
    
    protected void addBeanId(String key){
    	String beanid = key;
    	int index = beanid.indexOf("::");
    	if(index>0){
    		beanid = beanid.substring(0,index);
    	}
    	if(this.contains(beanid)) throw new RuntimeException("Duplicate BeanId["+beanid+"]");
    	
    }
    protected void fillBeanNames(Map<String,String> beans){
    	for(String key:beans.keySet()) this.addBeanId(key);
    }

    protected void buildSingletonBean(Map<String,String> beans,Map<String,String> attrs){
    	Map<String,String> st = querySingletonBeanDefine( beans);
    	for(String key :st.keySet()) beans.remove(key);
    	
    	
    }
   
   
   
   public static BeanFactory build(BeanFactory parent ,Properties config){
	   Map<String,String> beans = new HashMap<String,String>();
	   Map<String,String> attrs = new HashMap<String,String>();
	   fillBeanAndAttribute(config,beans,attrs);	   
	   BeanFactory result = new BeanFactory();	   
	   if(parent!=null) result.parent = parent;	   
	   result.fillBeanNames(beans);
	   result.buildSingletonBean(beans, attrs);
	   
	   
	   
	   return result;	   
   } 
    
   protected static Map<String,String> querySingletonBeanDefine(Map<String,String> beans){
	   Map<String,String> result = new HashMap<String,String>();

	   for(Map.Entry<String,String>entry:beans.entrySet()){
		   String key = entry.getKey();
		   if(key.indexOf("::prototype")<0){
			 result.put(key,entry.getValue());
		   }
	   }
	   return result;   
   }
   
   protected static void fillBeanAndAttribute(Properties config,Map<String,String> beans,Map<String,String> attrs){
	   for(Map.Entry<Object,Object> entry:config.entrySet()){
		  String key = (String) entry.getKey();
		  String value =(String) entry.getValue();
		  int index = key.indexOf("::");
		  String name = key;
		  if(index>=0){
			  if(index==0) raiseConfigException(key, value);
		  }else{
			 name = key.substring(0, index);
			 index = name.indexOf(".");
			 if(index<0){
				 beans.put(key, value);
			 }else if(index==0){
				 raiseConfigException(key,value);
			 }else{
				 attrs.put(key, value);
			 }
		  } 
	   }
   }  
    
    
    
    
    
    protected Object buildBean(String id){
    	return null;
    }
    
    
    
    private Map<String,Object> singleton = new HashMap<String,Object>();
    
    private BeanFactory parent = null;
    
    private List<String> names = new ArrayList<String>();
    private List<String> names4Singleton = new ArrayList<String>();
    
    protected BeanFactory(){}
    
    
    protected static void raiseConfigException(String key ,String val){
    	if(val==null)throw new RuntimeException("invalid bean config with["+key+"] not associate value");
    	throw new RuntimeException("invalid bean config with["+key+"="+val+"]");
    }
}
