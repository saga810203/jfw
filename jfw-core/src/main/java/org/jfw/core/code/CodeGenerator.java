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

import org.jfw.core.code.generator.annotations.ThreadSafe;

public class CodeGenerator {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private File sourcePath;
    private boolean overried;
    private Map<Object,Object> attribute = new HashMap<Object,Object>();

    private CodeGenerator(File sourcePath, boolean overried) {
        this.sourcePath = sourcePath;
        this.overried = overried;
    }

    public File getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(File sourcePath) {
        this.sourcePath = sourcePath;
    }

    public boolean isOverried() {
        return overried;
    }

    public void setOverried(boolean overried) {
        this.overried = overried;
    }
    public void setAttribute(Object key,Object value){
        this.attribute.put(key, value);
    }
    public Object getAttribute(Object key){
        return this.attribute.get(key);
    }
    public boolean hasAttribute(Object key){
        return this.attribute.containsKey(key);
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

    public static String getPkgName4Generator(Class<?> clazz) {
        String pkgName = clazz.getPackage().getName();
        String subPkgName = null;
        if (clazz.isInterface()) {
            subPkgName = "impl";
        } else if (null==clazz.getAnnotation(ThreadSafe.class) || clazz.getAnnotation(ThreadSafe.class).value())  {
            subPkgName = "extendz";
        }else {
        	subPkgName="wrap";
        }
        if (pkgName != null && pkgName.length() > 0) {
            return pkgName + "." + subPkgName;
        } else {
            return subPkgName;
        }
    }

    public static String getClassName4Generator(Class<?> clazz) {
        String sName = clazz.getSimpleName();

        if (clazz.isInterface()) {
            return sName + "Impl";
        } else if (null==clazz.getAnnotation(ThreadSafe.class) || clazz.getAnnotation(ThreadSafe.class).value()) {
            return sName + "Extends";
        } else {
        	return sName+"Wrap";
        }
    }

    private File getTargetFile(String packageName, String className) throws IOException {
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
        targetFile = new File(targetFile, className+".java");
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
    private String getParentClass(Class<?> clazz){
    	  if (clazz.isInterface()) {
              return " implements "+clazz.getName();
          } else if (null==clazz.getAnnotation(ThreadSafe.class) || clazz.getAnnotation(ThreadSafe.class).value()) {
              return " extends "+clazz.getName();
          } else {
          	return "";
          }
    }

    public void handle(Class<?> clazz) throws Exception {
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
        if (null == targetFile)
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";").append("public class ").append(simpleClassName).append(this.getParentClass(clazz)).append("{");
        for (Map.Entry<Method, MethodCodeGenerator> entry : ms.entrySet()) {
            try {
                entry.getValue().init(clazz, entry.getKey(),this);
                sb.append(entry.getValue().build());
            } catch (Exception e) {
                throw new Exception("处理方法(" + entry.getKey().getName() + ")错误", e);
            }
        }
        sb.append("}");
        this.saveClass(targetFile, sb.toString());
    }

    private static void listClass(File path, String prefix, List<String> list) {
        File[] files = path.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String fn = file.getName();
                fn = fn.substring(0, fn.length() - 6);
                if (fn.indexOf('$') < 0) {
                    if (prefix == null || prefix.length() == 0) {
                        list.add(fn);
                    } else {
                        list.add(prefix + "." + fn);
                    }
                }
            } else if (file.isDirectory()){
                if (prefix == null || prefix.length() == 0) {
                    listClass(file, file.getName(), list);
                } else {
                    listClass(file, prefix + "." + file.getName(), list);
                }
            }
        }
    }

    public static CodeGenerator getHander(File sourcePath, boolean overried) {
        return new CodeGenerator(sourcePath, overried);
    }

    public static void handle(File sourcePath, File classPath, boolean overried) throws Exception {
        CodeGenerator handler = new CodeGenerator(sourcePath, overried);
        List<String> list = new LinkedList<String>();
        listClass(classPath, null, list);
        for (String fn : list) {
            Class<?> clazz = Class.forName(fn);
            try {
                handler.handle(clazz);
            } catch (Exception e) {
                throw new Exception("处理类（" + clazz.getName() + ")错误", e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        File sourcePath = new File("E:\\EclipseProject\\ISS_WorkSpace\\jfw\\jfw-core\\src\\test\\java");
        File classPath = new File("E:\\EclipseProject\\ISS_WorkSpace\\jfw\\jfw-core\\target\\test-classes");
        handle(sourcePath, classPath, true);
        System.out.println("============OK=================");
    }
}
