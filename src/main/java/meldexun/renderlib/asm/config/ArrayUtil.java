package meldexun.renderlib.asm.config;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ArrayUtil {

	public static boolean[] toBooleanArray(String[] stringArray) {
		boolean[] array = new boolean[stringArray.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = Boolean.parseBoolean(stringArray[i]);
		}
		return array;
	}

	public static String[] fromBooleanArray(boolean[] array) {
		String[] stringArray = new String[array.length];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = Boolean.toString(array[i]);
		}
		return stringArray;
	}

	public static byte[] toByteArray(String[] stringArray) {
		byte[] array = new byte[stringArray.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = Byte.parseByte(stringArray[i]);
		}
		return array;
	}

	public static String[] fromByteArray(byte[] array) {
		String[] stringArray = new String[array.length];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = Byte.toString(array[i]);
		}
		return stringArray;
	}

	public static short[] toShortArray(String[] stringArray) {
		short[] array = new short[stringArray.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = Short.parseShort(stringArray[i]);
		}
		return array;
	}

	public static String[] fromShortArray(short[] array) {
		String[] stringArray = new String[array.length];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = Short.toString(array[i]);
		}
		return stringArray;
	}

	public static int[] toIntArray(String[] stringArray) {
		int[] array = new int[stringArray.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = Integer.parseInt(stringArray[i]);
		}
		return array;
	}

	public static String[] fromIntArray(int[] array) {
		String[] stringArray = new String[array.length];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = Integer.toString(array[i]);
		}
		return stringArray;
	}

	public static long[] toLongArray(String[] stringArray) {
		long[] array = new long[stringArray.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = Long.parseLong(stringArray[i]);
		}
		return array;
	}

	public static String[] fromLongArray(long[] array) {
		String[] stringArray = new String[array.length];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = Long.toString(array[i]);
		}
		return stringArray;
	}

	public static char[] toCharArray(String[] stringArray) {
		char[] array = new char[stringArray.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = (char) Short.parseShort(stringArray[i]);
		}
		return array;
	}

	public static String[] fromCharArray(char[] array) {
		String[] stringArray = new String[array.length];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = Short.toString((short) array[i]);
		}
		return stringArray;
	}

	public static float[] toFloatArray(String[] stringArray) {
		float[] array = new float[stringArray.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = Float.parseFloat(stringArray[i]);
		}
		return array;
	}

	public static String[] fromFloatArray(float[] array) {
		String[] stringArray = new String[array.length];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = Float.toString(array[i]);
		}
		return stringArray;
	}

	public static double[] toDoubleArray(String[] stringArray) {
		double[] array = new double[stringArray.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = Double.parseDouble(stringArray[i]);
		}
		return array;
	}

	public static String[] fromDoubleArray(double[] array) {
		String[] stringArray = new String[array.length];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = Double.toString(array[i]);
		}
		return stringArray;
	}

	public static <T> String[] toStringArray(T[] t_array) {
		return toStringArray(t_array, Object::toString);
	}

	public static <T> String[] toStringArray(T[] t_array, Function<T, String> mappingFunction) {
		return mapArray(t_array, mappingFunction, String[]::new);
	}

	public static <T, R> R[] mapArray(T[] t_array, Function<T, R> mappingFunction, IntFunction<R[]> generator) {
		R[] r_array = generator.apply(t_array.length);
		for (int i = 0; i < r_array.length; i++) {
			r_array[i] = mappingFunction.apply(t_array[i]);
		}
		return r_array;
	}

	public static Object toEnumArray(String[] stringArray, Class<?> componentType) {
		Object array = Array.newInstance(componentType, stringArray.length);
		for (int i = 0; i < stringArray.length; i++) {
			Array.set(array, i, EnumUtil.valueOf(componentType, stringArray[i]));
		}
		return array;
	}

	public static String[] fromEnumArray(Enum<?>[] array) {
		return mapArray(array, Enum::name, String[]::new);
	}

}
