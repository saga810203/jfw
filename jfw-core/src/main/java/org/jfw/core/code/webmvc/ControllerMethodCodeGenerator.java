package org.jfw.core.code.webmvc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;

import org.jfw.core.code.CodeGenerator;
import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.generator.annotations.webmvc.Controller;
import org.jfw.core.code.utils.Utils;

public class ControllerMethodCodeGenerator implements MethodCodeGenerator {
    private static final String DISPATCHER_FILE = "jfw_web_dispatcher.properties";

    protected Class<?> sourceClass;
    protected Method method;
    protected Controller ctrl;
    protected Handler[] handlers;
    protected StringBuilder sb;
    private boolean readedStringArray = false;
    private boolean readedString = false;
    private boolean readedHeaders = false;
    private boolean readedSession = false;
    private boolean readedOut = false;
    private boolean readedURI = false;
    private CodeGenerator cg = null;

    public CodeGenerator getCg() {
        return cg;
    }

    public void setCg(CodeGenerator cg) {
        this.cg = cg;
    }

    public void readURI(String pathAttribute) {
        if (!readedURI) {
            readedURI = true;
            sb.append("String[] uriArray = (String[]) req.getAttribute(\"").append(pathAttribute.trim()).append("\");");
        }
    }

    public void readOut() {
        if (!readedOut) {
            readedOut = true;
            sb.append("java.io.PrintWriter out = res.getWriter();");
        }
    }

    public void readSession() {
        if (!readedSession) {
            readedSession = true;
            sb.append(" javax.servlet.http.HttpSession session = req.getSession();");
        }
    }

    public void readParameters(String paramName) {
        if (!readedStringArray) {
            readedStringArray = true;
            sb.append("String[] ");
        }
        sb.append(" params = req.getParameters(\"").append(paramName).append("\");");
    }

    public void readHeaders(String paramName) {
        if (!this.readedHeaders) {
            readedHeaders = true;
            sb.append("java.util.List<Stirng> headers = new java.util.LinkedList<String>();");
            sb.append("java.util.Enumeration<String> ");
        }
        sb.append("enumHeaders = req.getHeaders(\"").append(paramName).append("\");\r\n");
        sb.append("headers.clear();\r\n");
        sb.append("while(enumHeaders.hasMoreElements()){\r\n").append("  headers.add(enumHeaders.nextElement());\r\n")
                .append("}\r\n");
        if (!readedStringArray) {
            readedStringArray = true;
            sb.append("String[] ");
        }
        sb.append("params =headers.toArray(new String[headers.size()]);");
    }

    public void readParameter(String paramName) {
        if (!readedString) {
            readedString = true;
            sb.append("String ");
        }
        sb.append(" param = req.getParameter(\"").append(paramName).append("\");");
    }

    public void readHeader(String paramName) {
        if (!readedString) {
            readedString = true;
            sb.append("String ");
        }
        sb.append("param = req.getHeader(\"").append(paramName).append("\");");
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    private void checkDispatcherFile() {
        if (!cg.hasAttribute(ControllerMethodCodeGenerator.class)) {
            File dispatcher = new File(cg.getSourcePath(), DISPATCHER_FILE);
            if (dispatcher.exists()) {
                if (!dispatcher.delete())
                    throw new RuntimeException("无法删除文件[" + dispatcher.getAbsolutePath() + "]");
            }
            cg.setAttribute(ControllerMethodCodeGenerator.class, Boolean.TRUE);
        }
    }
    private void addDispatcher(String className,String method ,String url) throws IOException{
        File dispatcher = new File(cg.getSourcePath(), DISPATCHER_FILE);
            RandomAccessFile randomFile = new RandomAccessFile(dispatcher, "rw");
            try{
                long fileLength = randomFile.length();
                //将写文件指针移到文件尾。
                randomFile.seek(fileLength);
                randomFile.writeUTF(url.trim()+"="+className.trim()+":"+method.trim()+"\r\n");
                randomFile.close();
            }finally{
                    randomFile.close();              
            }
    }

    @Override
    public void init(Class<?> parentType, Method method, CodeGenerator cg) {
        this.cg = cg;
        this.checkDispatcherFile();

        this.method = method;
        this.sourceClass = parentType;
        this.ctrl = method.getAnnotation(Controller.class);
        if (this.ctrl.value() == null)
            throw new RuntimeException("@Controller 属性value不能为空");
        Class<? extends Handler>[] cls = this.ctrl.handlers();
        this.handlers = new Handler[cls.length];
        for (int i = 0; i < cls.length; ++i) {
            try {
                this.handlers[i] = cls[i].newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            this.handlers[i].init(this);
        }

    }

    @Override
    public String build() {
        Utils.resetLocalVarName();
        if (this.ctrl.value() == null)throw new RuntimeException("@Controller'value is not an empty string");
        try {
            this.addDispatcher(CodeGenerator.getPkgName4Generator(sourceClass).trim()+"."+CodeGenerator.getClassName4Generator(this.sourceClass).trim(), this.method.getName(), this.ctrl.value());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sb = new StringBuilder();
        sb.append("public void ").append(this.method.getName())
                .append("(javax.servlet.http.HttpServletRequest req,javax.servlet.http.HttpServletResponse res) ")
                .append(" throws javax.servlet.ServletException,java.io.IOException{\r\n");
        if (this.method.getGenericReturnType() != void.class) {
            Utils.writeNameOfType(this.method.getGenericReturnType(), sb);
            sb.append(" result");
            if (!this.method.getReturnType().isPrimitive())
                sb.append(" = null;");
        }
        for (int i = 0; i < this.handlers.length; ++i) {
            this.handlers[i].appendBeforCode(sb);
        }
        for (int i = this.handlers.length - 1; i >= 0; --i) {
            this.handlers[i].appendAfterCode(sb);
        }
        sb.append("\r\n}\r\n");
        return sb.toString();
    }

}
