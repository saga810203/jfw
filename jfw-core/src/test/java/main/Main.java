package main;
import java.io.File;
import java.io.IOException;

import org.jfw.core.code.CodeGenerator;
public class Main {

    public static void main(String[] args) throws Exception{
        File sourcePath = new File("E:\\EclipseProject\\ISS_WorkSpace\\jfw\\jfw-core\\src\\test\\java");
        File classPath =  new File("E:\\EclipseProject\\ISS_WorkSpace\\jfw\\jfw-core\\target\\test-classes");
        CodeGenerator.handle(sourcePath,classPath,true);
        System.out.println("============OK=================");
    }
	

}
