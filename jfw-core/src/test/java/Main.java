import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.jfw.core.code.MethodCodeGenerator;
import org.jfw.core.code.generator.annotations.orm.SelectTable;
public class Main {
    private Boolean ss;
    private boolean bb;

	public boolean isBb() {
        return bb;
    }

    public void setBb(boolean bb) {
        this.bb = bb;
    }

    public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	//	System.out.println(String[].class.getName());
		for(Method m: Main.class.getMethods()){
			if (m.getName().equals("test"))
			{
//				Type t = m.getGenericReturnType();
//				StringBuilder sb = new StringBuilder();
//			MethodGenerator.writeNameOfType(t, sb);
//			System.out.println(sb.toString());
			   Annotation[] ats=   m.getAnnotations();
			   for(Annotation at:ats)
			   {
			       boolean bm = false;
			       for(Method am:at.getClass().getMethods())
			       {
			           if(am.getName().equals("bulidMehtod")&&
			              am.getReturnType()==boolean.class &&
			              am.getParameterTypes().length==0 &&
			              am.invoke(at, new Object[]{}).equals(Boolean.TRUE)){
			              bm=true;
			           }
			       }
			       if(bm){
	                   for(Method am:at.getClass().getMethods())
	                   {
	                       if(am.getName().equals("buildHandleClass")&&
	                          am.getReturnType()==Class.class &&
	                          am.getParameterTypes().length==0 &&
	                          MethodCodeGenerator.class.isAssignableFrom((Class<?>)  am.invoke(at, new Object[]{})  )){
	                              System.out.println(am.invoke(at, new Object[]{}));
	                       }
	                   }  
			       }
			   }

			}
		}
//        //SqlVal sv = new SqlVal();
        System.out.println(Integer.class.isPrimitive());

	}
	@SelectTable(bulidMehtod=true)
	public static void test(List<?> aa)
	{
		
	}

    public Boolean getSs() {
        return ss;
    }

    public void setSs(Boolean ss) {
        this.ss = ss;
    }

}
