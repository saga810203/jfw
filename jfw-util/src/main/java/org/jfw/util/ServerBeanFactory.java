package org.jfw.util;

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

    private static Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
    private static LinkedList<Object> servers = new LinkedList<Object>();

    private static Properties load() throws IOException {
        return ResourceUtil.readClassResource(CONF_FILE, new MultiInputStreamHandler<Properties>() {
            private Properties props = new Properties();

            public void handle(InputStream in) throws IOException {
                Properties tmp = new Properties();
                tmp.load(in);
                props.putAll(tmp);
            }

            public Properties get() {
                return props;
            }
        }, null);
    }

    private static void parseClass(String str, List<Class<?>> list) throws ClassNotFoundException {
        if (str == null || (str.trim().length() == 0))
            return;
        String[] clsnames = str.split(",");
        for (String clsname : clsnames) {
            if (clsname.trim().length() > 0) {
                list.add(Class.forName(clsname));
            }
        }
    }

    private static List<Class<?>> readClass() throws IOException, ClassNotFoundException {
        Properties props = load();
        List<Class<?>> list = new LinkedList<Class<?>>();
        DependSortService<Class<?>> dss = new DependSortService<Class<?>>();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            Class<?> cls = Class.forName(key.trim());
            list.clear();
            parseClass(val, list);
            dss.add(cls, list);
        }
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
        while (servers.size() > 0) {
            Object obj = servers.pollLast();
            shutdown(obj);
            destroy(obj);
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
