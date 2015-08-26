package org.jfw.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import org.jfw.util.io.MultiInputStreamHandler;
import org.jfw.util.io.ResourceUtil;
import org.jfw.util.sort.DependSortService;

public class ServerBeanFactory {
    public static String CONF_FILE = "jfw_server_conf.properties";
    public static String CONF_DEPEND_FILE="jfw_server_depends.properties";

    private static Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
    private static LinkedList<Object> servers = new LinkedList<Object>();
    private static Map<Class<?>, List<Class<?>>> loadDepends() throws Exception {
        return ResourceUtil.readClassResource(CONF_DEPEND_FILE,
                new MultiInputStreamHandler<Map<Class<?>, List<Class<?>>>>() {
                    private Map<Class<?>, List<Class<?>>> depClass = new HashMap<Class<?>, List<Class<?>>>();
                    private  List<Class<?>> parseClass(String str) throws ClassNotFoundException {
                        if (str == null || (str.trim().length() == 0))
                            return null;
                        List<Class<?>> list = new LinkedList<Class<?>>();
                        String[] clsnames = str.split(",");
                        for (String clsname : clsnames) {
                            if (clsname.trim().length() > 0) {
                                list.add(Class.forName(clsname));
                            }
                        }
                        return list;
                    }
                    public void handle(InputStream in) throws IOException, ClassNotFoundException {
                        Properties props = new Properties();
                        props.load(in);
                        for (Map.Entry<Object, Object> entry : props.entrySet()) {
                            String key = (String) entry.getKey();
                            String val = (String) entry.getValue();
                            Class<?> cls = Class.forName(key.trim());
                            List<Class<?>> list = parseClass(val);
                            depClass.put(cls, list);
                        }
                    }
                    public Map<Class<?>, List<Class<?>>> get() {
                        return depClass;
                    }
                }, null);
    }

    private static List<Class<?>> load() throws Exception {
        return ResourceUtil.readClassResource(CONF_FILE, new MultiInputStreamHandler<List<Class<?>>>() {
            private List<Class<?>> list = new LinkedList<Class<?>>();       
            public void handle(InputStream in) throws Exception {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                int len = 0;
                byte[] buf = new byte[4096];
                while ((len = in.read(buf)) >= 0) {
                    os.write(buf, 0, len);
                }
                String content = new String(os.toByteArray(), ConstData.UTF8);
                String[] strs = content.split("\n");
                for (String s : strs) {
                    s = s.trim();
                    if (s.length() > 0)
                        list.add(Class.forName(s));
                }
            }
            public List<Class<?>> get() {
                return list;
            }
        }, null);
    }
    


    private static List<Class<?>> readClass() throws Exception {
        Map<Class<?>,List<Class<?>>> depClass = loadDepends();        
        DependSortService<Class<?>> dss = new DependSortService<Class<?>>();
       dss.add(load(), depClass);
        return dss.sort();
    }

    private static void startup(Object obj) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Class<?> cls = obj.getClass();
        for (Method method : cls.getMethods()) {
            if (method.getName().equals("startup") && (0 == method.getParameterTypes().length)) {
                method.invoke(obj, ConstData.EMPTY_OBJECT_ARRAY);
                return;
            }
        }
        throw new IllegalArgumentException("Class<" + cls.getName() + "> not found method: void start()");
    }

    private static void shutdown(Object obj) {
        Class<?> cls = obj.getClass();
        for (Method method : cls.getMethods()) {
            if (method.getName().equals("shutdown") && (0 == method.getParameterTypes().length)) {
                try {
                    method.invoke(obj, ConstData.EMPTY_OBJECT_ARRAY);
                    return;
                } catch (Exception e) {
                }
            }
        }
    }

    public static void shutdown() {
        LinkedList<Object> list = new LinkedList<Object>();
        
        while (servers.size() > 0) {
            Object obj = servers.pollLast();
            shutdown(obj);
            list .add(obj);
        }
        while(list.size()>0){
            destroy(list.pollFirst());
        }
        map.clear();
    }

    private static void init(Object obj) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Class<?> cls = obj.getClass();
        for (Method method : cls.getMethods()) {
            if (method.getName().equals("init") && (0 == method.getParameterTypes().length)) {
                method.invoke(obj, ConstData.EMPTY_OBJECT_ARRAY);
                return;
            }
        }
    }
    private static void destroy(Object obj) {
        Class<?> cls = obj.getClass();
        for (Method method : cls.getMethods()) {
            if (method.getName().equals("destroy") && (0 == method.getParameterTypes().length)) {
                try {
                    method.invoke(obj, ConstData.EMPTY_OBJECT_ARRAY);
                } catch (Exception e) {
                }
                break;
            }
        }
    }

    public static void startup() throws Exception {
        try {
            List<Class<?>> list = readClass();
            for (ListIterator<Class<?>> it = list.listIterator(); it.hasNext();) {
                Class<?> cls = it.next();
                Object obj = cls.newInstance();
                init(obj);
                map.put(cls, obj);
                servers.add(obj);
            }
            for (ListIterator<Object> it = servers.listIterator(); it.hasNext();) {
               startup(it.next());
            }
        } catch (Exception e) {
            shutdown();
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> cls) {
        return (T) map.get(cls);
    }

}
