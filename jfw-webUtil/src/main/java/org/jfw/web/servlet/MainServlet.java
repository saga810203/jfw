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

import org.jfw.util.WebUtil;


public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = -2950792293111339623L;
    private static final Class<?>[] CTRL_METHOD_PARAM_TYPE=new Class<?>[]{HttpServletRequest.class,HttpServletResponse.class};
    public static final String REQ_MATCH_URI ="org.jfw.web.reqMacthUri";
    public static final String REQ_MATCH_URI_DYN ="org.jfw.web.reqMacthUri_DYN";
    
    
    private static final Map<String,ControllerMethod> staticUrls = new HashMap<String,ControllerMethod>();
    private static final ControllerMethod[][] dynamicUrls = new ControllerMethod[100][0];
    
    
    public static int prefixLen = 0;
    
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

    private void addControllerMethod(String methodName,String url,Object ctrl) throws Exception{
        Method method =null;        
        try {
            method =ctrl.getClass().getMethod(methodName, CTRL_METHOD_PARAM_TYPE);
        } catch (Exception e) {
            this.getServletContext().log("load controll error:"+ctrl.getClass().getName()+"."+methodName, e);
            throw e;
        }  
        String[] urlp = url.split("/");
        for(String str:urlp)
        {
            if(str.startsWith("{")&& str.endsWith("}")){
                this.addDynamicUrlControllerMehtod(urlp, method, ctrl);
                return;
            }
        }
        staticUrls.put(url,new ControllerMethod(ctrl,method));        
    }
    private void addDynamicUrlControllerMehtod(String[] url,Method method,Object ctrl){
        for( int i = 0 ; i < url.length ; ++i){
            if(url[i].startsWith("{")&& url[i].endsWith("}")){
                url[i] = null;
            }
        }
        ControllerMethod[] cms = dynamicUrls[url.length];
        if(cms.length == 0){
            cms = new ControllerMethod[1];
            cms[0] = new ControllerMethod(ctrl,method,url);
            dynamicUrls[url.length] = cms;
        }else{
            dynamicUrls[url.length] = new ControllerMethod[cms.length+1];
            System.arraycopy(cms,0,dynamicUrls[url.length],0,cms.length);
            dynamicUrls[url.length][cms.length]= new ControllerMethod(ctrl,method,url);
        }        
    }

    
    private void buildControllerMethods(Properties props) throws Exception
    {
        Map<String,Object> map = this.createController(props);
        for(Map.Entry<Object,Object> entry:props.entrySet()){
            String url =((String)entry.getKey()).trim();
            String value =((String) entry.getValue()).trim();
            int index = value.indexOf(":");
            String className = value.substring(0,index).trim();
            String method = value.substring(index+1).trim();
            Object ctrl = map.get(className);
            if(null!=ctrl){
                this.addControllerMethod(method, url, ctrl);
            }
        }
    }
   

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqUri = WebUtil.normalize(req.getRequestURI());
        reqUri = reqUri.substring(prefixLen);
        req.setAttribute(REQ_MATCH_URI, reqUri);
        ControllerMethod cm = staticUrls.get(reqUri);
        if(null!=cm){
            cm.invoke(req, resp);
        }else{
            String[] reqUris = reqUri.split("/");
            ControllerMethod[] cms = dynamicUrls[reqUris.length];
            for(int i = 0 ; i < cms.length ; ++i){
                if(cms[i].match(reqUris)){
                    req.setAttribute(REQ_MATCH_URI_DYN, reqUris);
                    cms[i].invoke(req, resp);
                    return;
                }
            }
           resp.sendError(404); 
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init() throws ServletException {


        try {
            this.buildControllerMethods(this.readDispather());
        } catch (Exception e) {
            // TODO Handler Exception
            e.printStackTrace();
        }
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
