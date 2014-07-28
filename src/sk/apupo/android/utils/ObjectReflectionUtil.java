package sk.apupo.android.utils;

import java.lang.reflect.Method;

public class ObjectReflectionUtil {

	public ObjectReflectionUtil() {
	}
	
	public static Method getMethod(Class<?> cls, String name, Class<?>[] parameterTypes) {
		try {
			return cls.getMethod(name, parameterTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return null;
	}


}
