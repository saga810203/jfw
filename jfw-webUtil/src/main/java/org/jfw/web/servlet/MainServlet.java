package org.jfw.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfw.util.WebControllerContext;


public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = -2950792293111339623L;

    
    public static final String PREFIX_LEN = "prefixLen";
    public static final String VIEW_TYPE ="viewType";
    public static final String SERVLET_INIT_ERROR_HANDLER="servletInitErrorHandler";
    public static final String SERVLET_NOT_FOUND_HANDLER="servletNotFoundHandler";
    public static final String DO_FRAMEWORK_INIT="doFrameWorkInit";
    
    private boolean running = true;
    private Throwable initError = null;
    private boolean doInit = false;
    private  int prefixLen = 0;
    private  int viewType = 0;
    private ServletInitExceptionHandler sieh;
    private ServletNotFoundExceptionHandler snfeh;
     public int getPrefixLen() {
        return prefixLen;
    }
    public int getViewType() {
        return viewType;
    }
    public ServletInitExceptionHandler getSieh() {
        return sieh;
    }
    public ServletNotFoundExceptionHandler getSnfeh() {
        return snfeh;
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       if (this.running){
           if(!WebControllerContext.execute(req, resp, this.prefixLen, this.viewType)){
               this.snfeh.handle(this, req, resp);
           }
       }else{
         this.sieh.handle(this, req, resp, initError);  
       }   
    }

    @Override
    public void destroy() {
        if(doInit) WebControllerContext.destroy();
    }

    private void readConfigParamter(){
        try{
            this.prefixLen = Integer.parseInt(this.getInitParameter(PREFIX_LEN));
        }catch(Exception e){
            this.prefixLen = 0;            
        }
        try{
            this.viewType = Integer.parseInt(this.getInitParameter(VIEW_TYPE));
        }catch(Exception e){
            this.viewType = 0;            
        }
        String strInit = this.getInitParameter(DO_FRAMEWORK_INIT);
        this.doInit="1".equals(strInit)||"true".equalsIgnoreCase(strInit)||"yes".equalsIgnoreCase(strInit);
    }

    private void readErrorHandler(){
        try{
            this.sieh = (ServletInitExceptionHandler)(Class.forName(this.getInitParameter(SERVLET_INIT_ERROR_HANDLER)).newInstance());
        }catch(Exception e){
           this.sieh = null;   
           this.getServletContext().log("create servlet init exception handler error ,will be use default handler", e);
        }
        if(null == this.sieh){
            this.sieh = new ServletInitExceptionHandler() {
                @Override
                public void handle(MainServlet servlet, HttpServletRequest req, HttpServletResponse resp, Throwable th)
                        throws ServletException, IOException {
                    resp.setStatus(500);
                    th.printStackTrace(resp.getWriter());                    
                }
            };
        }
        try{
            this.snfeh = (ServletNotFoundExceptionHandler)(Class.forName(this.getInitParameter(SERVLET_NOT_FOUND_HANDLER)).newInstance());
        }catch(Exception e){
           this.snfeh = null;   
           this.getServletContext().log("create servlet not found handler error ,will be use default handler", e);
        }
        if(null == this.snfeh){
            this.snfeh = new ServletNotFoundExceptionHandler() {
                @Override
                public void handle(MainServlet servlet, HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                        IOException {
                   resp.sendError(404);                    
                }
            };
        }
    }
    @Override
    public void init() throws ServletException {
         this.readConfigParamter();
         if(this.doInit){
             try{
                WebControllerContext.init();
             }catch(Exception e){
                 this.initError = e;
             }
         }else{
             this.initError = WebControllerContext.getError();
         }
         this.running = null==this.initError;   
         this.readErrorHandler();
    }
  
    public static abstract class ServletInitExceptionHandler{
        public abstract void handle(MainServlet servlet,HttpServletRequest req, HttpServletResponse resp,Throwable th) throws ServletException,IOException;
    }
    public static abstract class ServletNotFoundExceptionHandler{
        public abstract void handle(MainServlet servlet,HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException;
    }

}
