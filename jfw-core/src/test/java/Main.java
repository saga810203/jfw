import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.jfw.core.code.MethodGenerator;


public class Main {
    private Boolean ss;
    private boolean bb;

	public boolean isBb() {
        return bb;
    }

    public void setBb(boolean bb) {
        this.bb = bb;
    }

    public static void main(String[] args) {
	//	System.out.println(String[].class.getName());
		for(Method m: Main.class.getMethods()){
			if (m.getName().equals("test"))
			{
				Type t = m.getGenericParameterTypes()[0];
				StringBuilder sb = new StringBuilder();
			MethodGenerator.writeNameOfType(t, sb);
			System.out.println(sb.toString());
			}
		}

	}
	
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
