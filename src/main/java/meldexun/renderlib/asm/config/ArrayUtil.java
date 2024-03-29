package meldexun.renderlib.asm.config;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

class ArrayUtil {

	static boolean[] toBooleanArray(List<String> list) {
		boolean[] array = new boolean[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = Boolean.parseBoolean(list.get(i));
		}
		return array;
	}

	static byte[] toByteArray(List<String> list) {
		byte[] array = new byte[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = Byte.parseByte(list.get(i));
		}
		return array;
	}

	static short[] toShortArray(List<String> list) {
		short[] array = new short[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = Short.parseShort(list.get(i));
		}
		return array;
	}

	static int[] toIntegerArray(List<String> list) {
		int[] array = new int[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = Integer.parseInt(list.get(i));
		}
		return array;
	}

	static long[] toLongArray(List<String> list) {
		long[] array = new long[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = Long.parseLong(list.get(i));
		}
		return array;
	}

	static char[] toCharArray(List<String> list) {
		char[] array = new char[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = (char) Short.parseShort(list.get(i));
		}
		return array;
	}

	static float[] toFloatArray(List<String> list) {
		float[] array = new float[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = Float.parseFloat(list.get(i));
		}
		return array;
	}

	static double[] toDoubleArray(List<String> list) {
		double[] array = new double[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = Double.parseDouble(list.get(i));
		}
		return array;
	}

	static <T> T[] toArray(List<String> list, Function<String, T> parser, IntFunction<T[]> generator) {
		T[] array = generator.apply(list.size());
		for (int i = 0; i < array.length; i++) {
			array[i] = parser.apply(list.get(i));
		}
		return array;
	}

	static String[] toStringArray(List<String> list) {
		return list.toArray(new String[list.size()]);
	}

	static Object toEnumArray(List<String> list, Class<?> componentType) {
		Object array = Array.newInstance(componentType, list.size());
		for (int i = 0; i < list.size(); i++) {
			Array.set(array, i, EnumUtil.valueOf(componentType, list.get(i)));
		}
		return array;
	}

}
