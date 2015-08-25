package org.jfw.core.code.generator.annotations;

public @interface PO {
	String name();
	boolean readonly() default false;
	boolean enableDeleted() default true;
}
