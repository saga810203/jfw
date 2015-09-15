package org.jfw.core.code.webmvc.handler;

import org.jfw.core.code.generator.annotations.webmvc.SetSessionAttribute;
import org.jfw.core.code.webmvc.Handler;

public class SetSessionAttributeHandler extends Handler{

	@Override
	public void appendBeforCode(StringBuilder sb) {
		SetSessionAttribute sa= this.getMethodAnnotation(SetSessionAttribute.class);
		if(null==sa || (this.getCmcg().getMethod().getReturnType()==void.class))return;
		this.getCmcg().readSession();
		sb.append("session.setAttriubte(\"").append(sa.attributeName().trim()).append("\",result");
		if(sa.value().trim().length()>0){
			sb.append(".").append(sa.value().trim());
		}
		sb.append(");\r\n");
	}

}
