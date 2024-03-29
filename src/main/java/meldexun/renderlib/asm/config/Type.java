package meldexun.renderlib.asm.config;

import java.lang.reflect.Field;
import java.util.List;

enum Type {

	BOOLEAN {
		@Override
		void setField(Object object, Field field, String value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(boolean.class)) {
				field.setBoolean(object, Boolean.parseBoolean(value));
				return;
			}
			if (fieldType.equals(Boolean.class)) {
				field.set(object, Boolean.valueOf(value));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for boolean entry " + field.getName());
		}

		@Override
		void setListField(Object object, Field field, List<String> value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(boolean[].class)) {
				field.set(object, ArrayUtil.toBooleanArray(value));
				return;
			}
			if (fieldType.equals(Boolean[].class)) {
				field.set(object, ArrayUtil.toArray(value, Boolean::valueOf, Boolean[]::new));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for boolean array entry " + field.getName());
		}
	},
	INTEGER {
		@Override
		void setField(Object object, Field field, String value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(byte.class)) {
				field.setByte(object, Byte.parseByte(value));
				return;
			}
			if (fieldType.equals(Byte.class)) {
				field.set(object, Byte.valueOf(value));
				return;
			}
			if (fieldType.equals(short.class)) {
				field.setShort(object, Short.parseShort(value));
				return;
			}
			if (fieldType.equals(Short.class)) {
				field.set(object, Short.valueOf(value));
				return;
			}
			if (fieldType.equals(int.class)) {
				field.setInt(object, Integer.parseInt(value));
				return;
			}
			if (fieldType.equals(Integer.class)) {
				field.set(object, Integer.valueOf(value));
				return;
			}
			if (fieldType.equals(long.class)) {
				field.setLong(object, Long.parseLong(value));
				return;
			}
			if (fieldType.equals(Long.class)) {
				field.set(object, Long.valueOf(value));
				return;
			}
			if (fieldType.equals(char.class)) {
				field.setChar(object, (char) Short.parseShort(value));
				return;
			}
			if (fieldType.equals(Character.class)) {
				field.set(object, Character.valueOf((char) Short.parseShort(value)));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for int entry " + field.getName());
		}

		@Override
		void setListField(Object object, Field field, List<String> value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(byte[].class)) {
				field.set(object, ArrayUtil.toByteArray(value));
				return;
			}
			if (fieldType.equals(Byte[].class)) {
				field.set(object, ArrayUtil.toArray(value, Byte::valueOf, Byte[]::new));
				return;
			}
			if (fieldType.equals(short[].class)) {
				field.set(object, ArrayUtil.toShortArray(value));
				return;
			}
			if (fieldType.equals(Short[].class)) {
				field.set(object, ArrayUtil.toArray(value, Short::valueOf, Short[]::new));
				return;
			}
			if (fieldType.equals(int[].class)) {
				field.set(object, ArrayUtil.toIntegerArray(value));
				return;
			}
			if (fieldType.equals(Integer[].class)) {
				field.set(object, ArrayUtil.toArray(value, Integer::valueOf, Integer[]::new));
				return;
			}
			if (fieldType.equals(long[].class)) {
				field.set(object, ArrayUtil.toLongArray(value));
				return;
			}
			if (fieldType.equals(Long[].class)) {
				field.set(object, ArrayUtil.toArray(value, Long::valueOf, Long[]::new));
				return;
			}
			if (fieldType.equals(char[].class)) {
				field.set(object, ArrayUtil.toCharArray(value));
				return;
			}
			if (fieldType.equals(Character[].class)) {
				field.set(object, ArrayUtil.toArray(value, s -> Character.valueOf((char) Short.parseShort(s)), Character[]::new));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for int array entry " + field.getName());
		}
	},
	DOUBLE {
		@Override
		void setField(Object object, Field field, String value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(float.class)) {
				field.setFloat(object, Float.parseFloat(value));
				return;
			}
			if (fieldType.equals(Float.class)) {
				field.set(object, Float.valueOf(value));
				return;
			}
			if (fieldType.equals(double.class)) {
				field.setDouble(object, Double.parseDouble(value));
				return;
			}
			if (fieldType.equals(Double.class)) {
				field.set(object, Double.valueOf(value));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for double entry " + field.getName());
		}

		@Override
		void setListField(Object object, Field field, List<String> value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(float[].class)) {
				field.set(object, ArrayUtil.toFloatArray(value));
				return;
			}
			if (fieldType.equals(Float[].class)) {
				field.set(object, ArrayUtil.toArray(value, Float::valueOf, Float[]::new));
				return;
			}
			if (fieldType.equals(double[].class)) {
				field.set(object, ArrayUtil.toDoubleArray(value));
				return;
			}
			if (fieldType.equals(Double[].class)) {
				field.set(object, ArrayUtil.toArray(value, Double::valueOf, Double[]::new));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for double array entry " + field.getName());
		}
	},
	STRING {
		@Override
		void setField(Object object, Field field, String value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(String.class)) {
				field.set(object, value);
				return;
			}
			if (fieldType.isEnum()) {
				field.set(object, EnumUtil.valueOf(fieldType, value));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for String entry " + field.getName());
		}

		@Override
		void setListField(Object object, Field field, List<String> value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(String[].class)) {
				field.set(object, ArrayUtil.toStringArray(value));
				return;
			}
			if (fieldType.isEnum()) {
				field.set(object, ArrayUtil.toEnumArray(value, fieldType));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for String array entry " + field.getName());
		}
	};

	static Type get(char c) {
		switch (c) {
		case 'B':
			return BOOLEAN;
		case 'I':
			return INTEGER;
		case 'D':
			return DOUBLE;
		case 'S':
			return STRING;
		default:
			throw new IllegalArgumentException("Char '" + c + "' is not a valid type identifier");
		}
	}

	abstract void setField(Object object, Field field, String value) throws ReflectiveOperationException;

	abstract void setListField(Object object, Field field, List<String> value) throws ReflectiveOperationException;

}
