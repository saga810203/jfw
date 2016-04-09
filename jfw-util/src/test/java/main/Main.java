package main;

import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception{
//        String json = "[1,2,3,4]";
//        List<Integer> list = org.jfw.util.json.JsonService.<List<Integer>>fromJson(json,new org.jfw.util.reflect.TypeReference<List<Integer>>(){}.getType() );
//        for(Integer i:list){
//            System.out.println(i);
//        }
        
        String aa ="a,b;c";
        String[] s = aa.split("[,;]");
        for(int i = 0 ; i < s.length ; ++i)
            System.out.println(i+":"+s[i]);
        
        
       
    }

}
