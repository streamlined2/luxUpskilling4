package org.training.spring.ioc.context.utility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.training.spring.ioc.exception.NoDefaultConstructorForBean;
import org.training.spring.ioc.exception.NoSetterFoundException;
import org.training.spring.ioc.exception.SetterInvocationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Inspector {

	private static final String SETTER_PREFIX = "set";
	private static final Class<?>[] NO_ARGUMENT_TYPES = new Class[0];
	private static final Object[] NO_ARGUMENTS = new Object[0];

	public static <T> T spawnObject(Class<T> cl) {
		return spawnObject(cl, NO_ARGUMENT_TYPES, NO_ARGUMENTS);
	}

	public static <T> T spawnObject(Class<T> cl, Class<?>[] argTypes, Object... args) {
		try {
			return cl.getDeclaredConstructor(argTypes).newInstance(args);
		} catch (ReflectiveOperationException e) {
			throw new NoDefaultConstructorForBean(
					String.format("no default constructor found for class %s", cl.getName()));
		}
	}

	public static void setPropertyValue(Object obj, String propertyName, String value) {
		String setterName = getSetterName(propertyName);
		try {
			Class<?> parameterType = getPropertyType(obj, propertyName);
			Method setter = obj.getClass().getDeclaredMethod(setterName, parameterType);
			checkIfSetterValid(setter);
			setValue(obj, setter, parameterType, value);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new SetterInvocationException(
					String.format("no such method %s in class %s", setterName, obj.getClass().getName()));
		}
	}

	private static Class<?> getPropertyType(Object obj, String propertyName) {
		try {
			Field property = obj.getClass().getDeclaredField(propertyName);
			return property.getType();
		} catch (NoSuchFieldException e) {
			throw new NoSetterFoundException(
					String.format("can't find property %s in class %s", propertyName, obj.getClass().getName()));
		}

	}

	private static void checkIfSetterValid(Method setter) {
		Class<?>[] parameterTypes = setter.getParameterTypes();
		if (parameterTypes.length != 1) {
			throw new NoSetterFoundException(String.format("number of parameters %d for setter method %s should be 1",
					parameterTypes.length, setter.getName()));
		}
	}

	private static void setValue(Object obj, Method setter, Class<?> type, String value) {
		setter.setAccessible(true);
		try {
			if (type == boolean.class) {
				setter.invoke(obj, Boolean.valueOf(value));
			} else if (type == byte.class) {
				setter.invoke(obj, Byte.valueOf(value));
			} else if (type == short.class) {
				setter.invoke(obj, Short.valueOf(value));
			} else if (type == int.class) {
				setter.invoke(obj, Integer.valueOf(value));
			} else if (type == long.class) {
				setter.invoke(obj, Long.valueOf(value));
			} else if (type == float.class) {
				setter.invoke(obj, Float.valueOf(value));
			} else if (type == double.class) {
				setter.invoke(obj, Double.valueOf(value));
			} else if (type == char.class) {
				setter.invoke(obj, Character.valueOf(value.charAt(0)));
			} else if (type == String.class) {
				setter.invoke(obj, value);
			} else {
				throw new SetterInvocationException(
						String.format("property %s of unknown type %s", setter.getName(), type.getName()));
			}
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new SetterInvocationException(String.format("call to setter %s failed", setter.getName()));
		}
	}

	public static void setPropertyReference(Object obj, String propertyName, Object referencedBean) {
		String setterName = getSetterName(propertyName);
		try {
			Class<?> parameterType = getPropertyType(obj, propertyName);
			Method setter = obj.getClass().getDeclaredMethod(setterName, parameterType);
			checkIfSetterValid(setter);
			setReference(obj, setter, referencedBean);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new SetterInvocationException(
					String.format("no such method %s in class %s", setterName, obj.getClass().getName()));
		}
	}

	private static void setReference(Object obj, Method setter, Object referencedBean) {
		try {
			setter.invoke(obj, referencedBean);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new SetterInvocationException(String.format("can't set reference via setter %s", setter.getName()));
		}
	}

	private String getSetterName(String propertyName) {
		return SETTER_PREFIX + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

}
