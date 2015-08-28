import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;


public class Main {

	public static void main(String[] args) {
		System.out.println(String[].class.getName());
		for(Method m: Main.class.getMethods()){
			if (m.getName().equals("test"))
			{
				Type t = m.getGenericParameterTypes()[0];
				StringBuilder sb = new StringBu
			}
		}

	}
	
	public static void test(Map<String,String> aa)
	{
		
	}

}
