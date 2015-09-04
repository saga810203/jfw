package org.jfw.core.code;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CodeGenerator {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private File sourcePath;

    private boolean overried;

    private CodeGenerator(File sourcePath, boolean overried) {
        this.sourcePath = sourcePath;
        this.overried = overried;
    }

    public static MethodCodeGenerator getGenerator(Method method) {
        Annotation[] ats = method.getAnnotations();
        if (ats.length == 0)
            return null;
        for (Annotation at : ats) {
            Method am = null;
            try {
                am = at.getClass().getDeclaredMethod("bulidMehtod", new Class<?>[0]);
                if (am.getGenericReturnType() != boolean.class)
                    continue;
                if (!am.invoke(at, new Object[0]).equals(Boolean.TRUE))
                    continue;
                am = at.getClass().getDeclaredMethod("buildHandleClass", new Class<?>[0]);
                if (am.getReturnType() == Class.class) {
                    Class<?> cls = (Class<?>) am.invoke(at, new Object[0]);
                    if (MethodCodeGenerator.class.isAssignableFrom(cls))
                        return (MethodCodeGenerator) cls.newInstance();
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    private static String getPkgName4Generator(Class<?> clazz) {
        String pkgName = clazz.getPackage().getName();
        String subPkgName = null;
        if (clazz.isInterface()) {
            subPkgName = "impl";
        } else {
            subPkgName = "extends";
        }
        if (pkgName != null && pkgName.length() > 0) {
            return pkgName + "." + subPkgName;
        } else {
            return subPkgName;
        }
    }

    private static String getClassName4Generator(Class<?> clazz) {
        String sName = clazz.getSimpleName();

        if (clazz.isInterface()) {
            return sName + "Impl";
        } else {
            return sName + "Extends";
        }
    }

    private File getTargetFile(String packageName, String className) throws IOException
    {
        File targetFile = this.sourcePath;
        String[] ss = packageName.split("\\.");
        for (int i = 0; i < ss.length; ++i) {
            targetFile = new File(targetFile, ss[i].trim());
            if (!targetFile.exists()) {
                if (!targetFile.mkdir()) {
                    throw new IOException("mkdir error:" + targetFile.getAbsolutePath());
                }
            }
        }
        targetFile = new File(targetFile, className);
        if (targetFile.exists()) {
            if (!overried)
                return null;
            if (!targetFile.delete()) {
                throw new IOException("delete file error:" + targetFile.getAbsolutePath());
            }
        }
        return targetFile;
    }
    private void saveClass(File targetFile, String content) throws IOException {
        FileOutputStream os = new FileOutputStream(targetFile);
        try {
            os.write(content.getBytes(UTF8));
        } finally {
            try {
                os.close();
            } catch (IOException e) {
            }
        }
    }

    public void handle(Class<?> clazz) throws IOException {
        String packageName = getPkgName4Generator(clazz);
        String simpleClassName = getClassName4Generator(clazz);
        
        Method[] methods = clazz.getMethods();
        Map<Method, MethodCodeGenerator> ms = new HashMap<Method, MethodCodeGenerator>();
        for (Method method : methods) {
            MethodCodeGenerator mcg = getGenerator(method);
            if (mcg != null)
                ms.put(method, mcg);
        }
        if (ms.size() == 0)
            return;
        File targetFile = getTargetFile(packageName, simpleClassName);
        if (null== targetFile) return ;
        
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";").append("public class ").append(simpleClassName)
                .append("{");
        for (Map.Entry<Method, MethodCodeGenerator> entry : ms.entrySet()) {
            entry.getValue().init(clazz, entry.getKey());
            sb.append(entry.getValue().build());
        }
        sb.append("}");
        this.saveClass(targetFile, sb.toString());
    }
    private static void listClass(File path,String prefix,List<String> list)
    {
        File[] files = path.listFiles();
        for(File file:files)
        {
            if(file.isFile()&& file.getName().endsWith(".class")){
                String fn = file.getName();
                fn = fn.substring(0,fn.length()-6);
                if(fn.indexOf('$')<0){
                if(prefix==null||prefix.length()==0)
                {
                    list.add(fn);
                }else
                {
                    list.add(prefix+"."+fn);
                }
                }
            }else{
                if(prefix==null||prefix.length()==0)
                {
                   listClass(file,file.getName(),list);
                }else
                {
                    listClass(file,prefix+"."+file.getName(),list);
                }
            }
        }
    }
    
    public static CodeGenerator getHander(File sourcePath,boolean overried)
    {
        return new CodeGenerator(sourcePath, overried);
    }
    public static void handle(File sourcePath,File classPath,boolean overried) throws IOException, ClassNotFoundException
    {
        CodeGenerator handler = new CodeGenerator(sourcePath, overried);
        List<String> list = new LinkedList<String>();
        listClass(classPath,null,list);
        for(String fn:list){
            Class<?> clazz = Class.forName(fn);
            handler.handle(clazz);
        }
    }
}
