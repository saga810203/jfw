package org.jfw.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = -2950792293111339623L;
    private static final Map<String,ControllerMethod> staticUrls = new HashMap<String,ControllerMethod>();
    private static final ControllerMethod[][] dynamicUrls = new ControllerMethod[100][];
    
    private Properties readDispather()
    {
        Properties result = new Properties();
        try {
            Enumeration<URL> en = Thread.currentThread().getContextClassLoader().getResources("jfw_web_dispatcher.properties");
            while(en.hasMoreElements()){
                URL url = en.nextElement();
                URLConnection con = url.openConnection();
                InputStream in =con.getInputStream();
                try
                {
                    Properties props = new Properties();
                    props.load(in);
                    result.putAll(props);        
                }finally{
                    try{
                    in.close();}catch(IOException e){}
                }
            }
        
        } catch (IOException e) {
        }
        return result;
    }
    private Map<String,Object> createController(Properties props){
        Map<String,Object> result = new HashMap<String,Object>();
        for(Object val:props.values()){
            String strVal =(String)val;
            String classname = strVal.substring(0,strVal.indexOf(":")).trim();
            if(result.containsKey(classname)) continue;
            try {
                Object obj = Class.forName(classname).newInstance();
                result.put(classname,obj);
            } catch (Exception e) {
               this.getServletContext().log("init controller Object error:",e);
            } 
        }
        return result;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.service(req, resp);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init() throws ServletException {
        // TODO Auto-generated method stub
        super.init();
    }
    private static class ControllerMethod{
        private Object obj;
        private Method method;
        private final String[] dynamicUrl;
        private final int pathLen ;
        
        public ControllerMethod(Object obj,Method method){
            this.obj = obj;
            this.method = method;
            this.dynamicUrl = null;
            this.pathLen = 0;
        }
        public ControllerMethod(Object obj,Method method,String[] dynamicUrl){
            this.obj = obj;
            this.method = method;
            this.dynamicUrl = dynamicUrl;
            this.pathLen = this.dynamicUrl.length;
        }
        public boolean match(String[] urls){
            for(int i = 0 ; i < pathLen ; ++i){
                if(this.dynamicUrl[i]!=null && (!this.dynamicUrl.equals(urls[i]))) return false;
            }
            return true;
        }
        
        public Object getObj() {
            return obj;
        }
        public void setObj(Object obj) {
            this.obj = obj;
        }
        public Method getMethod() {
            return method;
        }
        public void setMethod(Method method) {
            this.method = method;
        }
        public void invoke(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
            try {
                this.method.invoke(obj, req,res);
            } catch (InvocationTargetException e) {
                Throwable th = e.getTargetException();
                if(th instanceof IOException){
                    throw (IOException)th;
                }else if(th instanceof ServletException){
                    throw (ServletException)th;
                }else{
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
