package org.jfw.core.code.webmvc.handler;

import org.jfw.core.code.generator.annotations.ThreadSafe;
import org.jfw.core.code.webmvc.Handler;
import org.jfw.core.code.utils.Utils;

public class ExecuteHandler extends Handler {
	@Override
	public void appendBeforCode(StringBuilder sb) {
	    if(this.getCmcg().getMethod().getReturnType()!=void.class){
	    	sb.append("result = ");
	    }
	    ThreadSafe ts = this.getClassAnnotation(ThreadSafe.class);
	    if(null!=ts && (!ts.value())){
	    	sb.append("(new ");
	    	Utils.writeNameOfType(this.getCmcg().getSourceClass(), sb);
	    	sb.append("()).");
	    }
	    sb.append(this.getCmcg().getMethod().getName()).append("(");
	    int count4Parameter = this.getCmcg().getMethod().getParameterTypes().length;
	    for(int i = 0 ; i < count4Parameter ; ++i)
	    {
	    	if(i!=0)sb.append(",");
	    	sb.append("param").append(i);
	    }
	    sb.append(");");
	}

}
