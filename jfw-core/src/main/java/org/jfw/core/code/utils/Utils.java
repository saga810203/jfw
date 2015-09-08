package org.jfw.core.code.utils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Locale;

public abstract class Utils {
	
	private static int indexForLocal = 1;
	
	public static String getFieldNameInMethod(String fieldName){
	    String fn = fieldName.trim(); 
	    StringBuilder sb = new StringBuilder();
        sb.append(fn.substring(0,1).toUpperCase(Locale.ENGLISH));
        if(fn.length()>1)
        {
            sb.append(fn.substring(1));
        }
        return sb.toString();
	}
	

	   public static String getGetter(String fieldName)
	    {
	        String fn = fieldName.trim();
	        StringBuilder sb = new StringBuilder();
	        sb.append("get").append(fn.substring(0,1).toUpperCase(Locale.ENGLISH));
	        if(fn.length()>1)
	        {
	            sb.append(fn.substring(1));
	        }
	        return sb.toString();
	    }
	public static String getSetter(String fieldName){
		String fn = fieldName.trim();
		StringBuilder sb = new StringBuilder();
		sb.append("set").append(fn.substring(0,1).toUpperCase(Locale.ENGLISH));
		if(fn.length()>1)
		{
			sb.append(fn.substring(1));
		}
		return sb.toString();
	}
	public static void resetLocalVarName()
	{
		indexForLocal =1;
	}
	
	public static String  getLocalVarName()
	{
		return "tmp_"+indexForLocal++;
	}
	public static void writeNameOfType(Type type,StringBuilder sb)
	{
			if (type instanceof Class)
			{
				Class<?> cl = (Class<?>)type;
				if(cl.isArray()){
				    writeNameOfType(cl.getComponentType(),sb);
					sb.append("[]");
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
				TypeVariable<?> tt =(TypeVariable<?>)type;
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
				sb.append("?");
				if(wt.getLowerBounds().length>0){
					sb.append(" super ");
					writeNameOfType(wt.getLowerBounds()[0], sb);
				}else if (Object.class != wt.getUpperBounds()[0]) {
					sb.append(" extends ");
					writeNameOfType(wt.getUpperBounds()[0], sb);
				}
				
			}
	}
}
