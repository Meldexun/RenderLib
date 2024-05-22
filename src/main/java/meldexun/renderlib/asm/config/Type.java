package meldexun.renderlib.asm.config;

import java.lang.reflect.Field;

public enum Type {

	BOOLEAN {
		@Override
		public void setField(Object object, Field field, String value) throws ReflectiveOperationException {
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
		public void setListField(Object object, Field field, String[] value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(boolean[].class)) {
				field.set(object, ArrayUtil.toBooleanArray(value));
				return;
			}
			if (fieldType.equals(Boolean[].class)) {
				field.set(object, ArrayUtil.mapArray(value, Boolean::valueOf, Boolean[]::new));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for boolean array entry " + field.getName());
		}

		@Override
		public String getField(Object object, Field field) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(boolean.class)) {
				return Boolean.toString(field.getBoolean(object));
			}
			if (fieldType.equals(Boolean.class)) {
				return field.get(object).toString();
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for boolean entry " + field.getName());
		}

		@Override
		public String[] getListField(Object object, Field field) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(boolean[].class)) {
				return ArrayUtil.fromBooleanArray((boolean[]) field.get(object));
			}
			if (fieldType.equals(Boolean[].class)) {
				return ArrayUtil.mapArray((Boolean[]) field.get(object), Object::toString, String[]::new);
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for boolean array entry " + field.getName());
		}
	},
	INTEGER {
		@Override
		public void setField(Object object, Field field, String value) throws ReflectiveOperationException {
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
		public void setListField(Object object, Field field, String[] value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(byte[].class)) {
				field.set(object, ArrayUtil.toByteArray(value));
				return;
			}
			if (fieldType.equals(Byte[].class)) {
				field.set(object, ArrayUtil.mapArray(value, Byte::valueOf, Byte[]::new));
				return;
			}
			if (fieldType.equals(short[].class)) {
				field.set(object, ArrayUtil.toShortArray(value));
				return;
			}
			if (fieldType.equals(Short[].class)) {
				field.set(object, ArrayUtil.mapArray(value, Short::valueOf, Short[]::new));
				return;
			}
			if (fieldType.equals(int[].class)) {
				field.set(object, ArrayUtil.toIntArray(value));
				return;
			}
			if (fieldType.equals(Integer[].class)) {
				field.set(object, ArrayUtil.mapArray(value, Integer::valueOf, Integer[]::new));
				return;
			}
			if (fieldType.equals(long[].class)) {
				field.set(object, ArrayUtil.toLongArray(value));
				return;
			}
			if (fieldType.equals(Long[].class)) {
				field.set(object, ArrayUtil.mapArray(value, Long::valueOf, Long[]::new));
				return;
			}
			if (fieldType.equals(char[].class)) {
				field.set(object, ArrayUtil.toCharArray(value));
				return;
			}
			if (fieldType.equals(Character[].class)) {
				field.set(object, ArrayUtil.mapArray(value, s -> Character.valueOf((char) Short.parseShort(s)), Character[]::new));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for int array entry " + field.getName());
		}

		@Override
		public String getField(Object object, Field field) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(byte.class)) {
				return Byte.toString(field.getByte(object));
			}
			if (fieldType.equals(Byte.class)) {
				return field.get(object).toString();
			}
			if (fieldType.equals(short.class)) {
				return Short.toString(field.getShort(object));
			}
			if (fieldType.equals(Short.class)) {
				return field.get(object).toString();
			}
			if (fieldType.equals(int.class)) {
				return Integer.toString(field.getInt(object));
			}
			if (fieldType.equals(Integer.class)) {
				return field.get(object).toString();
			}
			if (fieldType.equals(long.class)) {
				return Long.toString(field.getLong(object));
			}
			if (fieldType.equals(Long.class)) {
				return field.get(object).toString();
			}
			if (fieldType.equals(char.class)) {
				return Short.toString((short) field.getChar(object));
			}
			if (fieldType.equals(Character.class)) {
				return Short.toString((short) (char) field.get(object));
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for int entry " + field.getName());
		}

		@Override
		public String[] getListField(Object object, Field field) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(byte[].class)) {
				return ArrayUtil.fromByteArray((byte[]) field.get(object));
			}
			if (fieldType.equals(Byte[].class)) {
				return ArrayUtil.mapArray((Byte[]) field.get(object), Object::toString, String[]::new);
			}
			if (fieldType.equals(short[].class)) {
				return ArrayUtil.fromShortArray((short[]) field.get(object));
			}
			if (fieldType.equals(Short[].class)) {
				return ArrayUtil.mapArray((Short[]) field.get(object), Object::toString, String[]::new);
			}
			if (fieldType.equals(int[].class)) {
				return ArrayUtil.fromIntArray((int[]) field.get(object));
			}
			if (fieldType.equals(Integer[].class)) {
				return ArrayUtil.mapArray((Integer[]) field.get(object), Object::toString, String[]::new);
			}
			if (fieldType.equals(long[].class)) {
				return ArrayUtil.fromLongArray((long[]) field.get(object));
			}
			if (fieldType.equals(Long[].class)) {
				return ArrayUtil.mapArray((Long[]) field.get(object), Object::toString, String[]::new);
			}
			if (fieldType.equals(char[].class)) {
				return ArrayUtil.fromCharArray((char[]) field.get(object));
			}
			if (fieldType.equals(Character[].class)) {
				return ArrayUtil.mapArray((Character[]) field.get(object), c -> Short.toString((short) (char) c), String[]::new);
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for int array entry " + field.getName());
		}
	},
	DOUBLE {
		@Override
		public void setField(Object object, Field field, String value) throws ReflectiveOperationException {
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
		public void setListField(Object object, Field field, String[] value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(float[].class)) {
				field.set(object, ArrayUtil.toFloatArray(value));
				return;
			}
			if (fieldType.equals(Float[].class)) {
				field.set(object, ArrayUtil.mapArray(value, Float::valueOf, Float[]::new));
				return;
			}
			if (fieldType.equals(double[].class)) {
				field.set(object, ArrayUtil.toDoubleArray(value));
				return;
			}
			if (fieldType.equals(Double[].class)) {
				field.set(object, ArrayUtil.mapArray(value, Double::valueOf, Double[]::new));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for double array entry " + field.getName());
		}

		@Override
		public String getField(Object object, Field field) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(float.class)) {
				return Float.toString(field.getFloat(object));
			}
			if (fieldType.equals(Float.class)) {
				return field.get(object).toString();
			}
			if (fieldType.equals(double.class)) {
				return Double.toString(field.getDouble(object));
			}
			if (fieldType.equals(Double.class)) {
				return field.get(object).toString();
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for double entry " + field.getName());
		}

		@Override
		public String[] getListField(Object object, Field field) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(float[].class)) {
				return ArrayUtil.fromFloatArray((float[]) field.get(object));
			}
			if (fieldType.equals(Float[].class)) {
				return ArrayUtil.mapArray((Float[]) field.get(object), Object::toString, String[]::new);
			}
			if (fieldType.equals(double[].class)) {
				return ArrayUtil.fromDoubleArray((double[]) field.get(object));
			}
			if (fieldType.equals(Double[].class)) {
				return ArrayUtil.mapArray((Double[]) field.get(object), Object::toString, String[]::new);
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for double array entry " + field.getName());
		}
	},
	STRING {
		@Override
		public void setField(Object object, Field field, String value) throws ReflectiveOperationException {
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
		public void setListField(Object object, Field field, String[] value) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(String[].class)) {
				field.set(object, value);
				return;
			}
			if (fieldType.isEnum()) {
				field.set(object, ArrayUtil.toEnumArray(value, fieldType));
				return;
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for String array entry " + field.getName());
		}

		@Override
		public String getField(Object object, Field field) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(String.class)) {
				return (String) field.get(object);
			}
			if (fieldType.isEnum()) {
				return ((Enum<?>) field.get(object)).name();
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for String entry " + field.getName());
		}

		@Override
		public String[] getListField(Object object, Field field) throws ReflectiveOperationException {
			Class<?> fieldType = field.getType();
			if (fieldType.equals(String[].class)) {
				return (String[]) field.get(object);
			}
			if (fieldType.isEnum()) {
				return ArrayUtil.fromEnumArray((Enum<?>[]) field.get(object));
			}
			throw new ConfigLoadException("Invalid field type (" + fieldType.getName() + ") for String array entry " + field.getName());
		}
	};

	public static Type get(char c) {
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

	public static Type get(Field field) {
		Class<?> type = field.getType();
		if (type.isArray()) {
			type = type.getComponentType();
		}
		if (type.equals(boolean.class) || type.equals(Boolean.class)) {
			return BOOLEAN;
		}
		if (type.equals(byte.class) || type.equals(Byte.class)
				|| type.equals(short.class) || type.equals(Short.class)
				|| type.equals(int.class) || type.equals(Integer.class)
				|| type.equals(long.class) || type.equals(Long.class)
				|| type.equals(char.class) || type.equals(Character.class)) {
			return INTEGER;
		}
		if (type.equals(float.class) || type.equals(Float.class)
				|| type.equals(double.class) || type.equals(Double.class)) {
			return DOUBLE;
		}
		if (type.equals(String.class) || type.isEnum()) {
			return STRING;
		}
		throw new IllegalArgumentException("No matching type found for field with type " + type);
	}

	public abstract void setField(Object object, Field field, String value) throws ReflectiveOperationException;

	public abstract void setListField(Object object, Field field, String[] value) throws ReflectiveOperationException;

	public abstract String getField(Object object, Field field) throws ReflectiveOperationException;

	public abstract String[] getListField(Object object, Field field) throws ReflectiveOperationException;

}
