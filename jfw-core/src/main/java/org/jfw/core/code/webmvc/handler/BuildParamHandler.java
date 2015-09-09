package org.jfw.core.code.webmvc.handler;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.Handler;

public class BuildParamHandler extends Handler {
    private Type[] params;
    private Object annotationObj;

    public void init() {
        params = this.getCmcg().getMethod().getGenericParameterTypes();
    }

    // private boolean buildParameter(int i){
    //
    // }

    @SuppressWarnings("unchecked")
    private Class<BuildParameter> getBuilderParamClass(int i) {
        Object[] objs = this.getCmcg().getMethod().getParameterAnnotations()[i];
        for (Object obj : objs) {
            for (Method method : obj.getClass().getMethods()) {
                if (method.getName().equals("buildParamClass") && (method.getParameterTypes().length == 0)) {
                    try {
                        Object cls = method.invoke(obj, new Object[0]);
                        if ((cls instanceof Class) && (BuildParameter.class.isAssignableFrom((Class<?>) cls))) {
                            this.annotationObj = obj;
                            return ((Class<BuildParameter>) cls);
                        }
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public void appendBeforCode(StringBuilder sb) {
        for (int i = 0; i < this.params.length; ++i) {
            Class<BuildParameter> bp = this.getBuilderParamClass(i);
            if (null == bp) {
                // TODO:是否增加默认处理类;
                throw new RuntimeException("参数没有定义处理类：parameterIndex:" + i + ", method:"
                        + this.getCmcg().getSourceClass().getName() + "."+this.getCmcg().getMethod().getName());
            }
            try {
                BuildParameter bph = bp.newInstance();
                bph.build(sb, i, this.params[i],this.getCmcg(), annotationObj);
            } catch (Exception e) {
                throw new RuntimeException("类["+bp.getName()+"]不存在无参构造方法");
            } 
        }

    }

    public void appendAfterCode(StringBuilder sb) {
    }

    public static class BuildParameter {
        public void build(StringBuilder sb, int index, Type type, ControllerMethodCodeGenerator cmcg, Object annotation) {
        }
    }
}
