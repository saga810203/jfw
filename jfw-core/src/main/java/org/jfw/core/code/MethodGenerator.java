package org.jfw.core.code;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.jfw.core.code.generator.impl.Utils;

public abstract class MethodGenerator {
	protected Class<?> parentType;
	protected Method method;
	
	public void init(Class<?> parentType,Method method)
	{
		this.method = method;
		this.parentType = parentType;
	}
	
	public static void writeNameOfType(Type type,StringBuilder sb)
	{
			if (type instanceof Class)
			{
				Class<?> cl = (Class<?>)type;
				if(cl.isArray()){
					sb.append(cl.getComponentType().getName()).append("[]");
				}else {
					sb.append(cl.getName());
				}
			}else if (type instanceof GenericArrayType)
			{
				writeNameOfType(((GenericArrayType)type).getGenericComponentType(),sb);
				sb.append("[]");
			}else  if (type instanceof ParameterizedType)
			{
				ParameterizedType pt = (ParameterizedType)type;
				writeNameOfType(pt.getRawType(),sb);
				sb.append("<");
				Type[] ts = pt.getActualTypeArguments();
				for(int i =0;i<ts.length; ++i)
				{
					if(i!=0) sb.append(",");
					writeNameOfType(ts[i],sb);
				}
				sb.append(">");
			}else  if (type instanceof TypeVariable)
			{
				TypeVariable tt =(TypeVariable)type;
				sb.append(tt.getName()).append(" extends ");
				Type[] ts = tt.getBounds();
				for(int i = 0 ; i < ts.length ;++i)
				{
					if(i!=0 )sb.append(" & ");
					writeNameOfType(ts[i],sb);
				}				
			}else   if (type instanceof WildcardType)
			{
				WildcardType wt =(WildcardType)type;
				sb.append("?  ");
				if(wt.getUpperBounds().length>0){
					sb.append("extends ");
					writeNameOfType(wt.getUpperBounds()[0], sb);
				}else {
					sb.append("suppers ");
					writeNameOfType(wt.getUpperBounds()[0], sb);
				}
				
			}
	}
	
	public String build()
	{
		StringBuilder sb = new StringBuilder();
		Utils.resetLocalVarName();
		
		
		
		
		return sb.toString();
	}
	
	
	public static void main(String[] aa)
	{
		
	}
}
