package org.jfw.core.code.generator.annotations.webmvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.Handler;
import org.jfw.core.code.webmvc.handler.BuildParamHandler;
import org.jfw.core.code.webmvc.handler.ExecuteHandler;
import org.jfw.core.code.webmvc.handler.RemoveSessionAttributeHandler;
import org.jfw.core.code.webmvc.handler.SetSessionAttributeHandler;
import org.jfw.core.code.webmvc.handler.ViewHandler;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
	String value();
	 boolean bulidMehtod() default true;
	 Class<? extends MethodCodeGenerator> buildHandleClass() default ControllerMethodCodeGenerator.class;
	Class<? extends Handler>[] handlers() default {
		BuildParamHandler.class,
		ViewHandler.class,
		ExecuteHandler.class,
		SetSessionAttributeHandler.class,
		RemoveSessionAttributeHandler.class
	};	
}
