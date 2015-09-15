package org.jfw.core.code.generator.annotations.webmvc;

import org.jfw.core.code.webmvc.handler.ViewHandler;
import org.jfw.core.code.webmvc.handler.view.JsonView;

public @interface JsonBody {
   Class<? extends ViewHandler.BuildView> buildViewClass() default JsonView.class;
}
