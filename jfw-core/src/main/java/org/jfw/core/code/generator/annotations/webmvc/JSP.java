package org.jfw.core.code.generator.annotations.webmvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jfw.core.code.webmvc.handler.ViewHandler;
import org.jfw.core.code.webmvc.handler.view.JspView;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSP {
	String value();
	String prefix() default "";
	Class<? extends ViewHandler.BuildView> buildViewClass() default JspView.class;
}
