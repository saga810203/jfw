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
	private boolean readedStringArray= false;
	private boolean readedString = false;
	private boolean readedHeaders= false;
	private boolean readedSession=false;
	private boolean readedOut = false;
	private boolean readedURI= false;
	public void readURI(int skipChars){
	    if(0> skipChars) {
	    throw new RuntimeException("@PathVar  skipChars is not less zero");    
	    }
	        if(!readedURI){
	            readedURI = true;
	            sb.append("String uri =org.jfw.util.WebUtil.normalize(res.getRequestURI());\r\n")
	               .append("String[] uriArray = uri");
	            if(skipChars>0){
	            sb.append(".substring(").append(skipChars).append(")");
	            }
	            sb.append(".split(\"/\");\r\n");	            
	        }
	    }
	public void readOut(){
	    if(!readedOut){
	        readedOut = true;
	        sb.append("java.io.PrintWriter out = res.getWriter();");
	    }
	}
	public void readSession(){
	    if(!readedSession){
	        readedSession = true;
	        sb.append(" javax.servlet.http.HttpSession session = req.getSession();");
	    }
	}
	
	public void readParameters(String paramName)
	{
	    if (!readedStringArray) {
	        readedStringArray = true;
	        sb.append("String[] ");
	    }
	    sb.append(" params = req.getParameters(\"").append(paramName).append("\");");
	}
	public void readHeaders(String paramName){
	    if(!this.readedHeaders){
	        readedHeaders = true;
	        sb.append("java.util.List<Stirng> headers = new java.util.LinkedList<String>();");
	        sb.append("java.util.Enumeration<String> ");
	    }
        sb.append("enumHeaders = req.getHeaders(\"").append(paramName).append("\");\r\n");
        sb.append("headers.clear();\r\n");
        sb.append("while(enumHeaders.hasMoreElements()){\r\n")
           .append("  headers.add(enumHeaders.nextElement());\r\n")
           .append("}\r\n");
        if (!readedStringArray) {
            readedStringArray = true;
            sb.append("String[] ");
        }
        sb.append("params =headers.toArray(new String[headers.size()];");
        }
	   public void readParameter(String paramName)
	    {
	        if (!readedString) {
	            readedString = true;
	            sb.append("String ");
	        }
	        sb.append(" param = req.getParameter(\"").append(paramName).append("\");");
	    }
       public void readHeader(String paramName)
       {
           if (!readedString) {
               readedString = true;
               sb.append("String ");
           }
           sb.append("param = req.getHeader(\"").append(paramName).append("\");");
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
