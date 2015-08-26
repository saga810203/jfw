package org.jfw.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfw.util.io.MultiInputStreamHandler;
import org.jfw.util.io.ResourceUtil;

public abstract class WebControllerContext {
    private static final Class<?>[] CTRL_METHOD_PARAM_TYPE = new Class<?>[] { HttpServletRequest.class,
            HttpServletResponse.class };
    public static final String REQ_MATCH_URI = "org.jfw.web.reqMacthUri";
    public static final String REQ_MATCH_URI_DYN = "org.jfw.web.reqMacthUri_DYN";
    public static final String CONF_FILE = "jfw_web_dispatcher.properties";
    public static final String REQ_VIEW_TYPE = "org.jfw.web.reqViewType";

    private static final Map<String, ControllerMethod> staticUrls = new HashMap<String, ControllerMethod>();
    private static final ControllerMethod[][] dynamicUrls = new ControllerMethod[100][0];

    private static boolean inited = false;
    private static Throwable error = null;

    private static Properties readDispather() throws Exception {
        try {
            return ResourceUtil.readClassResource(CONF_FILE, new MultiInputStreamHandler<Properties>() {
                private Properties result = new Properties();

                public void handle(InputStream in) throws IOException, ClassNotFoundException {
                    Properties props = new Properties();
                    props.load(in);
                    result.putAll(props);
                }

                public Properties get() {
                    return result;
                }
            }, null);
        } catch (Exception e) {
            throw new Exception("read dispather config error", e);
        }

    }

    private static Map<String, Object> createController(Properties props) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        for (Object val : props.values()) {
            String strVal = (String) val;
            String classname = strVal.substring(0, strVal.indexOf(":")).trim();
            if (result.containsKey(classname))
                continue;
            try {
                Object obj = Class.forName(classname).newInstance();
                result.put(classname, obj);
            } catch (Exception e) {
                throw new Exception("init controller Object error:", e);
            }
        }
        return result;
    }

    private static void addControllerMethod(String methodName, String url, Object ctrl) throws Exception {
        Method method = null;
        try {
            method = ctrl.getClass().getMethod(methodName, CTRL_METHOD_PARAM_TYPE);
        } catch (Exception e) {
            throw new Exception("load controll error:" + ctrl.getClass().getName() + "." + methodName, e);
        }
        String[] urlp = url.split("/");
        for (String str : urlp) {
            if (str.startsWith("{") && str.endsWith("}")) {
                addDynamicUrlControllerMehtod(urlp, method, ctrl);
                return;
            }
        }
        staticUrls.put(url, new ControllerMethod(ctrl, method));
    }

    private static void addDynamicUrlControllerMehtod(String[] url, Method method, Object ctrl) {
        for (int i = 0; i < url.length; ++i) {
            if (url[i].startsWith("{") && url[i].endsWith("}")) {
                url[i] = null;
            }
        }
        ControllerMethod[] cms = dynamicUrls[url.length];
        if (cms.length == 0) {
            cms = new ControllerMethod[1];
            cms[0] = new ControllerMethod(ctrl, method, url);
            dynamicUrls[url.length] = cms;
        } else {
            dynamicUrls[url.length] = new ControllerMethod[cms.length + 1];
            System.arraycopy(cms, 0, dynamicUrls[url.length], 0, cms.length);
            dynamicUrls[url.length][cms.length] = new ControllerMethod(ctrl, method, url);
        }
    }

    private static void buildControllerMethods(Properties props) throws Exception {
        Map<String, Object> map = createController(props);
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String url = ((String) entry.getKey()).trim();
            String value = ((String) entry.getValue()).trim();
            int index = value.indexOf(":");
            String className = value.substring(0, index).trim();
            String method = value.substring(index + 1).trim();
            Object ctrl = map.get(className);
            if (null != ctrl) {
                addControllerMethod(method, url, ctrl);
            }
        }
    }

    synchronized public static void init() {
        if (inited)
            return;
        inited = true;
        try {
            InitBeanFactory.init();
        } catch (Exception e) {
            error = new Exception("process init bean error", e);
            return;
        }
        try {
            ServerBeanFactory.startup();
        } catch (Exception e) {
            error = new Exception("process server bean error", e);
            InitBeanFactory.destroy();
            return;
        }

        try {
            buildControllerMethods(readDispather());
        } catch (Exception e) {
            error = new Exception("process webController init error", e);
            ServerBeanFactory.shutdown();
            InitBeanFactory.destroy();
        }
    }

    synchronized public static void destroy() {
        ControllerMethod[] empty = new ControllerMethod[0];
        for (int i = 0; i < dynamicUrls.length; ++i) {
            dynamicUrls[i] = empty;
        }
        staticUrls.clear();
        ServerBeanFactory.shutdown();
        InitBeanFactory.destroy();
    }

    public static boolean execute(HttpServletRequest req, HttpServletResponse resp, int prefixLen, int viewType)
            throws ServletException, IOException {
        String reqUri = WebUtil.normalize(req.getRequestURI());
        reqUri = reqUri.substring(prefixLen);
        req.setAttribute(REQ_MATCH_URI, reqUri);
        ControllerMethod cm = staticUrls.get(reqUri);
        if (null != cm) {
            req.setAttribute(REQ_VIEW_TYPE, viewType);
            cm.invoke(req, resp);
            return true;
        } else {
            String[] reqUris = reqUri.split("/");
            ControllerMethod[] cms = dynamicUrls[reqUris.length];
            for (int i = 0; i < cms.length; ++i) {
                if (cms[i].match(reqUris)) {
                    req.setAttribute(REQ_MATCH_URI_DYN, reqUris);
                    req.setAttribute(REQ_VIEW_TYPE, viewType);
                    cms[i].invoke(req, resp);
                    return true;
                }
            }
            return false;
        }
    }

    public static Throwable getError() {
        return error;
    }

    private static class ControllerMethod {
        private Object obj;
        private Method method;
        private final String[] dynamicUrl;
        private final int pathLen;

        public ControllerMethod(Object obj, Method method) {
            this.obj = obj;
            this.method = method;
            this.dynamicUrl = null;
            this.pathLen = 0;
        }

        public ControllerMethod(Object obj, Method method, String[] dynamicUrl) {
            this.obj = obj;
            this.method = method;
            this.dynamicUrl = dynamicUrl;
            this.pathLen = this.dynamicUrl.length;
        }

        public boolean match(String[] urls) {
            for (int i = 0; i < pathLen; ++i) {
                if (this.dynamicUrl[i] != null && (!this.dynamicUrl.equals(urls[i])))
                    return false;
            }
            return true;
        }

        public void invoke(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
            try {
                this.method.invoke(obj, req, res);
            } catch (InvocationTargetException e) {
                Throwable th = e.getTargetException();
                if (th instanceof IOException) {
                    throw (IOException) th;
                } else if (th instanceof ServletException) {
                    throw (ServletException) th;
                } else {
                    throw new ServletException(th);
                }
            } catch (IllegalAccessException e) {
                throw new ServletException(e);
            } catch (IllegalArgumentException e) {
                throw new ServletException(e);
            }
        }

    }
}
