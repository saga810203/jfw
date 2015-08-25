package org.jfw.core.code.generator.annotations;

import org.jfw.core.code.generator.enums.DE;

public @interface DBField {
	String name() default "";
	String alias() default "";
	DE value();
}
