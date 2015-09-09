package org.jfw.core.code.webmvc;

import java.lang.annotation.Annotation;

public class Handler{
	private ControllerMethodCodeGenerator cmcg;
	final public ControllerMethodCodeGenerator getCmcg() {
		return cmcg;
	}
	final public void setCmcg(ControllerMethodCodeGenerator cmcg) {
		this.cmcg = cmcg;
	}
	final public void init(ControllerMethodCodeGenerator cmcg)
	{
		this.cmcg = cmcg;
		this.init();
	}
	final public <A extends Annotation> A getClassAnnotation(Class<A> annotationClass) {
		return cmcg.getSourceClass().getAnnotation(annotationClass);
	}
	final public <A extends Annotation> A getMethodAnnotation(Class<A> annotationClass) {
		return cmcg.getMethod().getAnnotation(annotationClass);
	}
	public void init(){}
	public  void appendBeforCode(StringBuilder sb){}
	public  void appendAfterCode(StringBuilder sb){}
	
}