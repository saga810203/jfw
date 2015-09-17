package main;
import java.io.File;
import java.io.IOException;

import org.jfw.core.code.CodeGenerator;
public class Main {

    public static void main(String[] args) throws Exception{
//        File sourcePath = new File("E:\\IOC\\jfw\\jfw-core\\src\\test\\java");
//        File classPath =  new File("E:\\IOC\\jfw\\jfw-core\\target\\test-classes");
//        CodeGenerator.handle(sourcePath,classPath,true);
//        System.out.println("============OK=================");
        boolean doo= true;
        for(int i = 0 ; i < 10 ; ++i){
            System.out.println(i);
            if(doo && i==5){
                doo = false;
                --i;
                continue;
            }
        }
    }
	

}
