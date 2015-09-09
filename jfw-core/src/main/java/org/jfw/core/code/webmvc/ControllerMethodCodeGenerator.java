package org.jfw.core.code.webmvc;

import java.lang.reflect.Method;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.generator.annotations.webmvc.Controller;
import org.jfw.core.code.utils.Utils;

public class ControllerMethodCodeGenerator implements MethodCodeGenerator{
	protected Class<?> sourceClass;
	protected Method method;
	protected Controller ctrl;
	protected Handler[] handlers;
	protected StringBuilder sb ;
	private boolean readStringArray= false;
	private boolean readString = false;
	
	public void readParameters(String paramName)
	{
	    if (!readStringArray) {
	        readStringArray = true;
	        sb.append("String[] ");
	    }
	    sb.append(" params = req.getParameters(\"").append(paramName).append("\");");
	}
	   public void readParameter(String paramName)
	    {
	        if (!readString) {
	            readString = true;
	            sb.append("String ");
	        }
	        sb.append(" param = req.getParameter(\"").append(paramName).append("\");");
	    }

	
	
	public Class<?> getSourceClass() {
		return sourceClass;
	}

	public void setSourceClass(Class<?> sourceClass) {
		this.sourceClass = sourceClass;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}


	
	

	@Override
	public void init(Class<?> parentType, Method method) {
		this.method = method;
		this.sourceClass = parentType;	
		this.ctrl = parentType.getAnnotation(Controller.class);	
		Class<? extends Handler>[] cls = this.ctrl.handlers();
		this.handlers = new Handler[cls.length];
		for(int i = 0 ; i < cls.length ; ++i){
			try {
				this.handlers[i]=cls[i].newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
			this.handlers[i].init(this);
		}
		
	}

	@Override
	public String build() {
		Utils.resetLocalVarName();
		
		
		sb = new StringBuilder();
		sb.append("public void ").append(this.method.getName())
		.append("(javax.servlet.http.HttpServletRequest req,javax.servlet.http.HttpServletResponse res) ")
		.append(" throws javax.servlet.ServletException,java.io.IOException{\r\n");
		if (this.method.getGenericReturnType()!= void.class){
			Utils.writeNameOfType(this.method.getGenericReturnType(), sb);
			sb.append(" result");
			if(!this.method.getReturnType().isPrimitive()) sb.append(" = null;");
		}
		for(int i  = 0 ; i < this.handlers.length ; ++i){
			this.handlers[i].appendBeforCode(sb);
		}
		for(int i  = this.handlers.length-1 ; i >=0 ;--i){
			this.handlers[i].appendAfterCode(sb);
		}		
		sb.append("\r\n}\r\n");
		return sb.toString();
	}

}
