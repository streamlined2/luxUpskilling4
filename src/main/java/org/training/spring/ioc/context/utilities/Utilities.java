package org.training.spring.ioc.context.utilities;

import java.lang.reflect.Field;

import org.training.spring.ioc.exception.SourceParseException;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utilities {

	@SneakyThrows
	public static void setPrimitiveTypeField(Object obj, Field field, String value) {
		Class<?> type = field.getType();
		field.setAccessible(true);
		if (type.isPrimitive()) {
			if (type == boolean.class) {
				field.setBoolean(obj, Boolean.valueOf(value));
			} else if (type == byte.class) {
				field.setByte(obj, Byte.valueOf(value));
			} else if (type == short.class) {
				field.setShort(obj, Short.valueOf(value));
			} else if (type == int.class) {
				field.setInt(obj, Integer.valueOf(value));
			} else if (type == long.class) {
				field.setLong(obj, Long.valueOf(value));
			} else if (type == float.class) {
				field.setFloat(obj, Float.valueOf(value));
			} else if (type == double.class) {
				field.setDouble(obj, Double.valueOf(value));
			} else if (type == char.class) {
				field.setChar(obj, Character.valueOf(value.charAt(0)));
			}
		} else {
			throw new SourceParseException(String.format("field %s must be of primitive type", field.getName()));
		}
	}

	@SneakyThrows
	public static String getPrimitiveTypeField(Object obj, Field field) {
		Class<?> type = field.getType();
		field.setAccessible(true);
		if (type.isPrimitive()) {
			if (type == boolean.class) {
				return String.valueOf(field.getBoolean(obj));
			} else if (type == byte.class) {
				return String.valueOf(field.getByte(obj));
			} else if (type == short.class) {
				return String.valueOf(field.getShort(obj));
			} else if (type == int.class) {
				return String.valueOf(field.getInt(obj));
			} else if (type == long.class) {
				return String.valueOf(field.getLong(obj));
			} else if (type == float.class) {
				return String.valueOf(field.getFloat(obj));
			} else if (type == double.class) {
				return String.valueOf(field.getDouble(obj));
			} else if (type == char.class) {
				return String.valueOf(field.getChar(obj));
			}
		}
		throw new SourceParseException(String.format("field %s must be of primitive type", field.getName()));
	}

	@SneakyThrows
	private static void initializeField(Object obj, Field field) {
		Class<?> type = field.getType();
		field.setAccessible(true);
		if (type.isPrimitive()) {
			if (type == boolean.class) {
				field.setBoolean(obj, false);
			} else if (type == byte.class) {
				field.setByte(obj, (byte) 0);
			} else if (type == short.class) {
				field.setShort(obj, (short) 0);
			} else if (type == int.class) {
				field.setInt(obj, 0);
			} else if (type == long.class) {
				field.setLong(obj, 0);
			} else if (type == float.class) {
				field.setFloat(obj, 0f);
			} else if (type == double.class) {
				field.setDouble(obj, 0D);
			} else if (type == char.class) {
				field.setChar(obj, '\0');
			}
		} else {
			field.set(obj, null);
		}
	}

}
