package org.jfw.core.code.generator.annotations.handler.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;













import java.util.Map;

import org.jfw.core.code.generator.annotations.orm.DBField;
import org.jfw.core.code.generator.annotations.orm.Table;
import org.jfw.core.code.generator.annotations.orm.View;

public abstract class POUtil {
    private static Map<Class<?>,Class<?>> wrapClass=new HashMap<Class<?>,Class<?>>();
    
    private static Class<?> getTableClass(Class<?> clazz)
    {
        Class<?> beanClass=clazz;
        while(beanClass!=Object.class&&(!beanClass.isInterface()))
        {
            Table table = beanClass.getAnnotation(Table.class);
            if(null!=table){
            if (table.abstracted()) throw new IllegalArgumentException("abstrace table is not query with class："+clazz.getName());
                return beanClass;
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;
    }
    private static Class<?> getViewClass(Class<?> clazz)
    {
        Class<?> beanClass=clazz;
        while(beanClass!=Object.class&&(!beanClass.isInterface()))
        {
            View table = beanClass.getAnnotation(View.class);
            if(null!=table){
                return beanClass;
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;
    }
    public static String GetTableName(Class<?> clazz)
    {
        Class<?> tableClass = getTableClass(clazz);
        if(null == tableClass) new IllegalArgumentException("not found table with class:"+clazz.getName());
        return tableClass.getAnnotation(Table.class).value();
    }
    public static String GetViewName(Class<?> clazz)
    {
        Class<?> tableClass = getViewClass(clazz);
        if(null == tableClass) new IllegalArgumentException("not found view with class:"+clazz.getName());
        return tableClass.getAnnotation(View.class).value();
    }
    public static Table getTableAnnotation(Class<?> clazz)
    {
    	Class<?> tclazz = getTableClass(clazz);
    	return tclazz.getAnnotation(Table.class);
    }
    public static View getViewAnnotation(Class<?> clazz)
    {
    	Class<?> tclazz = getViewClass(clazz);
    	return tclazz.getAnnotation(View.class);
    }
    
    public static void appendDBField(Class<?> clazz,List<Field> list)
    {
        if(clazz.isInterface() || (Object.class==clazz) ) return;
        appendDBField(clazz.getSuperclass(), list);
        Field[] fs = clazz.getDeclaredFields();
        for(Field f:fs)
        {
            if(Modifier.isStatic(f.getModifiers())) continue;
            if(Modifier.isFinal(f.getModifiers())) continue;
            
            DBField df = f.getAnnotation(DBField.class);
            if(null!=df && (df.xorInSelect()?!df.value().isInSelect():df.value().isInSelect())) list.add(f);            
        }
        
    }
    
    public static List<SelectField> getSelectFieldsInTable(Class<?> clazz)
    {
        Class<?> tableClass = getTableClass(clazz);
        if(null == tableClass) new IllegalArgumentException("not found table with class:"+clazz.getName());
        List<Field> list = new LinkedList<Field>();
        appendDBField(tableClass, list);
        List<SelectField> result = new LinkedList<SelectField>();
        for(Field f:list)
        {
            result.add(SelectField.build(f));
        }
        return result;
    }
    public static List<SelectField> getSelectFieldsInView(Class<?> clazz)
    {
        Class<?> tableClass = getViewClass(clazz);
        if(null == tableClass) new IllegalArgumentException("not found view with class:"+clazz.getName());
        List<Field> list = new LinkedList<Field>();
        Field[] fs = clazz.getDeclaredFields();
        for(Field f:fs)
        {
            if(Modifier.isStatic(f.getModifiers())) continue;
            if(Modifier.isFinal(f.getModifiers())) continue;
            
            DBField df = f.getAnnotation(DBField.class);
            if(null!=df && (df.xorInSelect()?!df.value().isInSelect():df.value().isInSelect())) list.add(f);            
        }
        List<SelectField> result = new LinkedList<SelectField>();
        for(Field f:list)
        {
            result.add(SelectField.build(f));
        }
        return result;
    }
    
    
    public static String fieldName2ColumnName(String fn)
    {
        StringBuilder sb = new StringBuilder();
        char[] fna = fn.trim().toCharArray();
        sb.append(fna[0]);
       for(int i  = 1 ; i < fna.length ; ++i)
       {
           if(Character.isUpperCase(fna[i])) sb.append("_");
           sb.append(fna[i]);
       }
       return sb.toString().toUpperCase(Locale.ENGLISH); 
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Class<?> getReturnType4SelectList(Method method)
    {
    	Type tt = method.getGenericReturnType();
    	if(tt instanceof Class){
    		Class cl = (Class)tt;
    		if(cl.isAssignableFrom(java.util.LinkedList.class)) return Object.class;
    		throw null;
    	}else if(tt instanceof  ParameterizedType){
    		ParameterizedType pt = (ParameterizedType)tt;
    		if(pt.getRawType() instanceof Class)
    		{
    			Class cl = (Class)pt.getRawType();
    			if(cl.isAssignableFrom(java.util.LinkedList.class) && pt.getActualTypeArguments().length==1 && (pt.getActualTypeArguments()[0] instanceof Class))
    			{
    				return (Class)pt.getActualTypeArguments()[0];
    			}
    		}
    		return null;
    	}
    	return null;
    }
	public static Class<?> getReturnType4SelectOne(Method method)
    {
    	Type tt = method.getGenericReturnType();
    	if(tt instanceof Class){
    		return (Class<?>)tt;
    	}
    	return null;
    }
	public static Class<?> getWrapClass(Class<?> clazz)
	{
	    return wrapClass.get(clazz);
	}
	public static Class<?> getPrimitiveClass(Class<?> clazz)
	{
	    for(Map.Entry<Class<?>,Class<?>> entry :wrapClass.entrySet())
	    {
	        if(clazz== entry.getValue() ) return entry.getKey();
	    }
	    
	    return null;
	}
    public static void appendInsertDBField(Class<?> clazz,List<Field> list)
    {
        if(clazz.isInterface() || (Object.class==clazz) ) return;
        appendDBField(clazz.getSuperclass(), list);
        Field[] fs = clazz.getDeclaredFields();
        for(Field f:fs)
        {
            if(Modifier.isStatic(f.getModifiers())) continue;
            if(Modifier.isFinal(f.getModifiers())) continue;
            
            DBField df = f.getAnnotation(DBField.class);
            if(null!=df && (!df.calcField())) list.add(f);            
        }
    }
    
    public static List<Field> getInsertFieldInTable(Class<?> clazz){
    	List<Field> list = new LinkedList<Field>();
    	Class<?> tCls = getTableClass(clazz);
    	if(tCls==null) throw new RuntimeException("not found @Table at Class or supperClass :"+clazz.getName());
    	appendInsertDBField(tCls,list);
    	return list;
    }
    public static void appendUpdateDBField(Class<?> clazz,List<Field> list)
    {
        if(clazz.isInterface() || (Object.class==clazz) ) return;
        appendDBField(clazz.getSuperclass(), list);
        Field[] fs = clazz.getDeclaredFields();
        for(Field f:fs)
        {
            if(Modifier.isStatic(f.getModifiers())) continue;
            if(Modifier.isFinal(f.getModifiers())) continue;
            
            DBField df = f.getAnnotation(DBField.class);
            if(null!=df && (df.xorInUpdate()?!df.value().isInUpdate():df.value().isInUpdate())) list.add(f);            
        }
    }
    
    
	
	
	
	static{
	    wrapClass.put(int.class,Integer.class);
	    wrapClass.put(byte.class,Byte.class);
	    wrapClass.put(short.class,Short.class);
	    wrapClass.put(float.class,Float.class);
	    wrapClass.put(double.class, Double.class);
	    wrapClass.put(boolean.class,Boolean.class);
	    wrapClass.put(char.class,Character.class);
	    wrapClass.put(long.class, Long.class);
	}
}
