package org.jfw.core.code.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.generator.annotations.orm.Final;
import org.jfw.core.code.utils.Utils;

public abstract class AbstractMethodCodeGenerator implements MethodCodeGenerator {
	protected Class<?> parentType;
	protected Method method;
	protected Annotation[] annotations;
	@Override
	public void init(Class<?> parentType,Method method)
	{
		this.method = method;
		if(!Modifier.isAbstract(method.getModifiers())&& !method.getDeclaringClass().isInterface()  ){
		    throw new RuntimeException(parentType.getName()+"."+method.getName()+ " is not abstract  mehtod");
		}
		this.parentType = parentType;
		this.annotations = method.getAnnotations();
		this.aferInit();
	}
	public abstract void aferInit();
	
	
	
	@Override
	public String build()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n@Override\r\n");
		Utils.resetLocalVarName();
	    int modifiers = this.method.getModifiers();
//        Modifier.PUBLIC         | Modifier.PROTECTED    | Modifier.PRIVATE |
//        Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.FINAL   |
//        Modifier.SYNCHRONIZED   | Modifier.NATIVE       | Modifier.STRICT;
	    if (Modifier.isSynchronized(modifiers)) sb.append("synchronized ");
	    if(null != this.method.getAnnotation(Final.class)) sb.append("final ");
	   sb.append("public ");
	    //if (Modifier.isStatic(modifiers)) sb.append("static ");
	    Utils.writeNameOfType(this.method.getGenericReturnType(), sb);
	    sb.append(" ").append(this.method.getName()).append("(");
	    this.buildParameters(sb);
	    sb.append(") ");
	    this.buildThrows(sb);
	    sb.append("\r\n{\r\n");
	    this.buildContent(sb);
	    sb.append("\r\n}\r\n");
		return sb.toString();
	}
	protected abstract void buildParameters(StringBuilder sb) ;
	 
	private void buildThrows(StringBuilder sb)
	{
	    Type[] ts = this.method.getGenericExceptionTypes();
	    if(ts.length>0){
	        sb.append(" throws ");
	        for(int i = 0 ; i < ts.length ;++i)
	        {
	            if(i != 0 )sb.append(",");
	            Utils.writeNameOfType(ts[i], sb);
	        }
	    }
	    
	}
	protected void buildContent(StringBuilder sb){
	    this.buildContentHead(sb);
	    this.buildContentBody(sb);
	    this.buildContentFoot(sb);
	}
	protected abstract void buildContentBody(StringBuilder sb);
	protected void buildContentHead(StringBuilder sb){	    
	}
	protected void buildContentFoot(StringBuilder sb){	        
	}
}
