package org.jfw.core.code.generator.annotations.webmvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.webmvc.handler.ViewHandler;
import org.jfw.core.code.webmvc.handler.view.ExpressionJspView;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpressionJSP {
	String defaultJsp();
	String defaultPrefix() default "";
	ELCondition[] condition();	
	Class<? extends ViewHandler.BuildView> buildViewClass() default ExpressionJspView.class;
}
