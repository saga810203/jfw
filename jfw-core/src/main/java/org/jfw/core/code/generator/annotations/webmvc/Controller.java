package org.jfw.core.code.generator.annotations.webmvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.Handler;
import org.jfw.core.code.webmvc.handler.ExecuteHandler;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
	String value();
	 boolean bulidMehtod() default true;
	 Class<? extends MethodCodeGenerator> buildHandleClass() default ControllerMethodCodeGenerator.class;
	Class<? extends Handler>[] handlers() default {
		
		ExecuteHandler.class
	};	
}
