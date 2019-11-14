package br.com.wiu.jutilw.copier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.OneToMany;

/***
 * @author wiu
 * @since 2019-11-14
 */
public class CopyProperties {

	public static void copyAll(Object source, Object target) {
		Field[] declaredFields = source.getClass().getDeclaredFields();
		List<Field> listFields = Arrays.asList(declaredFields);
		listFields.parallelStream().forEach(f -> {	
			f.setAccessible(true);
			verifyField(f, source, target);
		});		

	}

	private static void verifyField(Field f, Object source, Object target) {
		Class<?> clazzTarget = target.getClass();
		try {
			if(isAnIterable(f.getType())) {
				iterableTreatment(f, source, target);
				return;
			}
			Method setter = getSetMethod(clazzTarget, f, f.getType());
			setter.setAccessible(true);
			setter.invoke(target, f.get(source));
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			//e.printStackTrace();
		}		
	}

	private static void iterableTreatment(Field f, Object source, Object target) {
		Class<?> clazzTarget = target.getClass();
		try {
			Field declaredField = clazzTarget.getDeclaredField(f.getName());
			declaredField.setAccessible(true);
			List<Object> l  =  new ArrayList<Object>();
			for(Object o : (List<?>)declaredField.get(source)) {
				Object clone = clone(o);
				if(declaredField.isAnnotationPresent(ReferenceThisObject.class) || declaredField.isAnnotationPresent(OneToMany.class)) {
					Class<?> clazTarget = clone.getClass();
					for (Field field : clazTarget.getDeclaredFields()) {
						if(field.getType() == source.getClass()) {										
							Method setter = getSetMethod(clazTarget, field, field.getType());
							setter.setAccessible(true);
							setter.invoke(clone, source);
							break;
						}
					}
				}
				l.add(clone);							
			}
			declaredField.set(target, l);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}		
	}

	private static boolean isAnIterable(Class<?> type) {
		return type == List.class;
	}

	public static Object clone(Object o) {
		Object newInstance = null;
		try {
			Constructor<?> c = o.getClass().getDeclaredConstructor();
			c.setAccessible(true);
			c.newInstance();
		} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			//e.printStackTrace();
			for (Method method : o.getClass().getDeclaredMethods()) {
				if(method.isAnnotationPresent(NewInstanceOf.class)) {
					method.setAccessible(true);
					try {
						newInstance = method.invoke(o);
						break;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		copyAll(o, newInstance);

		return newInstance;
	}

	private static Method getSetMethod(Class<?> clazz, Field f, Class<?> clazzType) throws NoSuchMethodException, SecurityException {
		return clazz.getMethod("set" + 
				f.getName().replaceFirst(f.getName().substring(0, 1), f.getName().substring(0, 1).toUpperCase()), 
				clazzType);
	}
}
