package org.jfw.core.code.webmvc.handler.buildParam;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jfw.core.code.utils.Utils;

public abstract class TransferFactory {
    private final static String  RESOURCE="jfw_webmvc_req_transfer.properties";
    
    private final static Map<String,Class<RequestParamTransfer>> transfers = new HashMap<String,Class<RequestParamTransfer>>();
    
    
    @SuppressWarnings("unchecked")
    private final static void loadTransfer() {
        try {
            Enumeration<URL> en = Thread.currentThread().getContextClassLoader().getResources(RESOURCE);
            while(en.hasMoreElements()){
                URL url = en.nextElement();
                URLConnection con = url.openConnection();
                InputStream in =con.getInputStream();
                try
                {
                    Properties props = new Properties();
                    props.load(in);
                    for(Map.Entry<Object,Object> entry:props.entrySet())
                    {
                        String key  = (String)entry.getKey();
                        String val =(String)entry.getValue();
                        try{
                        transfers.put(key, (Class<RequestParamTransfer>)Class.forName(val));     
                        }catch(Exception e){
                            throw new RuntimeException(e);
                        }
                                           
                    }
                }finally{
                    try{
                    in.close();}catch(IOException e){}
                }
            }
        
        } catch (IOException e) {
  
        }
    }
    public static Class<RequestParamTransfer> getRequestParamTransfer(Type clazz) 
    {
        if(transfers.isEmpty()) loadTransfer();
        StringBuilder sb = new StringBuilder();
        Utils.writeNameOfType(clazz, sb);
        return  transfers.get(sb.toString());
    }
}
