package org.jfw.core.code.webmvc.handler;

import java.lang.reflect.Method;

import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.Handler;

public class ViewHandler extends Handler {
	 private Object annotationObj= null;
	 private BuildView  viewHandler = null;

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		Object[] objs = this.getCmcg().getMethod().getAnnotations();
		for (Object obj : objs) {
			for (Method method : obj.getClass().getMethods()) {
				if (method.getName().equals("buildViewClass") && (method.getParameterTypes().length == 0)) {
					try {
						Object cls = method.invoke(obj, new Object[0]);
						if ((cls instanceof Class) && (BuildView.class.isAssignableFrom((Class<?>) cls))) {
							this.annotationObj = obj;
							this.viewHandler =  ((Class<BuildView>) cls).newInstance();
						}
					} catch (Exception e) {
						this.viewHandler = null;
					}
				}
			}
		}
	}

	@Override
	public void appendBeforCode(StringBuilder sb) {
		if(null!=this.viewHandler){
			this.viewHandler.before(sb,this.getCmcg(), annotationObj);
		}
	}

	@Override
	public void appendAfterCode(StringBuilder sb) {
		if(null!=this.viewHandler){
			this.viewHandler.after(sb,this.getCmcg(), annotationObj);
		}
	}

	public static class BuildView {
		public void before(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
		}
		public void after(StringBuilder sb, ControllerMethodCodeGenerator cmcg, Object annotation) {
		}
		
	}
}
