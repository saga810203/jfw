package org.jfw.core.code.generator.orm.impl;

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
		return "tmp_"+indexForLocal;
	}
}
