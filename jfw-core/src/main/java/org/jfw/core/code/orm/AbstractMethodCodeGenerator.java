package org.jfw.core.code.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.generator.annotations.orm.Final;
import org.jfw.core.code.generator.orm.impl.Utils;

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
	@Override
	public String build()
	{
		StringBuilder sb = new StringBuilder();
		Utils.resetLocalVarName();
	    int modifiers = this.method.getModifiers();
//        Modifier.PUBLIC         | Modifier.PROTECTED    | Modifier.PRIVATE |
//        Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.FINAL   |
//        Modifier.SYNCHRONIZED   | Modifier.NATIVE       | Modifier.STRICT;
	    if (Modifier.isSynchronized(modifiers)) sb.append("synchronized ");
	    if(null != this.method.getAnnotation(Final.class)) sb.append("final ");
	   sb.append("public ");
	    //if (Modifier.isStatic(modifiers)) sb.append("static ");
	    writeNameOfType(this.method.getGenericReturnType(), sb);
	    sb.append(" ").append(this.method.getName()).append("(");
	    this.buildParameters(sb);
	    sb.append(") ");
	    this.buildThrows(sb);
	    sb.append("{");
	    this.buildContent(sb);
	    sb.append("}");
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
	            writeNameOfType(ts[i], sb);
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
