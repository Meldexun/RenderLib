package meldexun.renderlib.asm.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;

import javax.annotation.Nullable;

import net.minecraftforge.common.config.Config;

public class ConfigUtil {

	public static Field[] getFields(Object object) {
		return getFields(object, object.getClass());
	}

	public static Field[] getFields(Class<?> objectClass) {
		return getFields(null, objectClass);
	}

	public static Field[] getFields(@Nullable Object object, Class<?> objectClass) {
		return Arrays.stream(objectClass.getFields())
				.filter(field -> Modifier.isStatic(field.getModifiers()) == (object == null))
				.filter(field -> !field.isAnnotationPresent(Config.Ignore.class))
				.toArray(Field[]::new);
	}

	public static String getName(Field field) {
		String fieldName;
		Config.Name nameAnnotation = field.getAnnotation(Config.Name.class);
		if (nameAnnotation != null) {
			fieldName = nameAnnotation.value();
		} else {
			fieldName = field.getName();
		}
		if (isCategory(field)) {
			fieldName = fieldName.toLowerCase(Locale.ENGLISH);
		}
		return fieldName;
	}

	public static boolean isCategory(Field field) {
		Class<?> type = field.getType();
		if (type.isPrimitive())
			return false;
		if (type.equals(Boolean.class))
			return false;
		if (type.equals(Byte.class))
			return false;
		if (type.equals(Short.class))
			return false;
		if (type.equals(Integer.class))
			return false;
		if (type.equals(Long.class))
			return false;
		if (type.equals(Character.class))
			return false;
		if (type.equals(Float.class))
			return false;
		if (type.equals(Double.class))
			return false;
		if (type.equals(String.class))
			return false;
		if (type.isEnum())
			return false;
		if (type.isArray())
			return false;
		return true;
	}

}
