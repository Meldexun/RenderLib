package meldexun.renderlib.util.memory;

import static meldexun.matrixutil.UnsafeUtil.UNSAFE;

import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;

public class MemoryUtil {

	public static long allocateMemory(long capacity) {
		return UNSAFE.allocateMemory(capacity);
	}

	public static void freeMemory(long address) {
		UNSAFE.freeMemory(address);
	}

	private static <T> T allocateAndMap(long capacity, LongFunction<T> f) {
		long address = 0L;
		try {
			return f.apply(address = allocateMemory(capacity));
		} catch (Throwable t) {
			freeMemory(address);
			throw t;
		}
	}

	static <T> T allocateBuffer(long capacity, PrimitiveInfo type, BufferFactory<T> bufferFactory) {
		return allocateAndMap(type.toByte(capacity), address -> bufferFactory.create(address, capacity, type));
	}

	public static void copyMemory(long srcAddress, long destAddress, long bytes) {
		if (bytes <= 256) {
			int i = 0;
			for (; i < (bytes & ~7); i += 8) {
				UNSAFE.putLong(destAddress + i, UNSAFE.getLong(srcAddress + i));
			}
			if (i < (bytes & ~3)) {
				UNSAFE.putInt(destAddress + i, UNSAFE.getInt(srcAddress + i));
				i += 4;
			}
			if (i < (bytes & ~1)) {
				UNSAFE.putShort(destAddress + i, UNSAFE.getShort(srcAddress + i));
				i += 2;
			}
			if (i < bytes) {
				UNSAFE.putByte(destAddress + i, UNSAFE.getByte(srcAddress + i));
			}
		} else {
			UNSAFE.copyMemory(srcAddress, destAddress, bytes);
		}
	}

	public static void copyMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes) {
		if (bytes <= 256) {
			int i = 0;
			for (; i < (bytes & ~7); i += 8) {
				UNSAFE.putLong(destBase, destOffset + i, UNSAFE.getLong(srcBase, srcOffset + i));
			}
			if (i < (bytes & ~3)) {
				UNSAFE.putInt(destBase, destOffset + i, UNSAFE.getInt(srcBase, srcOffset + i));
				i += 4;
			}
			if (i < (bytes & ~1)) {
				UNSAFE.putShort(destBase, destOffset + i, UNSAFE.getShort(srcBase, srcOffset + i));
				i += 2;
			}
			if (i < bytes) {
				UNSAFE.putByte(destBase, destOffset + i, UNSAFE.getByte(srcBase, srcOffset + i));
			}
		} else {
			UNSAFE.copyMemory(srcBase, srcOffset, destBase, destOffset, bytes);
		}
	}

	public static void copyFromArray(byte[] array, int offset, Object destBase, long destOffset, int length) {
		copyFromArray(array, offset, destBase, destOffset, length, PrimitiveInfo.BYTE);
	}

	public static void copyFromArray(short[] array, int offset, Object destBase, long destOffset, int length) {
		copyFromArray(array, offset, destBase, destOffset, length, PrimitiveInfo.SHORT);
	}

	public static void copyFromArray(int[] array, int offset, Object destBase, long destOffset, int length) {
		copyFromArray(array, offset, destBase, destOffset, length, PrimitiveInfo.INT);
	}

	public static void copyFromArray(long[] array, int offset, Object destBase, long destOffset, int length) {
		copyFromArray(array, offset, destBase, destOffset, length, PrimitiveInfo.LONG);
	}

	public static void copyFromArray(float[] array, int offset, Object destBase, long destOffset, int length) {
		copyFromArray(array, offset, destBase, destOffset, length, PrimitiveInfo.FLOAT);
	}

	public static void copyFromArray(double[] array, int offset, Object destBase, long destOffset, int length) {
		copyFromArray(array, offset, destBase, destOffset, length, PrimitiveInfo.DOUBLE);
	}

	public static void copyFromArray(char[] array, int offset, Object destBase, long destOffset, int length) {
		copyFromArray(array, offset, destBase, destOffset, length, PrimitiveInfo.CHAR);
	}

	private static void copyFromArray(Object array, int offset, Object destBase, long destOffset, int length, PrimitiveInfo type) {
		copyMemory(array, type.arrayBaseOffset() + type.toByte(offset), destBase, destOffset, type.toByte(length));
	}

	public static void copyIntoArray(Object srcBase, long srcOffset, byte[] array, int offset, int length) {
		copyIntoArray(srcBase, srcOffset, array, offset, length, PrimitiveInfo.BYTE);
	}

	public static void copyIntoArray(Object srcBase, long srcOffset, short[] array, int offset, int length) {
		copyIntoArray(srcBase, srcOffset, array, offset, length, PrimitiveInfo.SHORT);
	}

	public static void copyIntoArray(Object srcBase, long srcOffset, int[] array, int offset, int length) {
		copyIntoArray(srcBase, srcOffset, array, offset, length, PrimitiveInfo.INT);
	}

	public static void copyIntoArray(Object srcBase, long srcOffset, long[] array, int offset, int length) {
		copyIntoArray(srcBase, srcOffset, array, offset, length, PrimitiveInfo.LONG);
	}

	public static void copyIntoArray(Object srcBase, long srcOffset, float[] array, int offset, int length) {
		copyIntoArray(srcBase, srcOffset, array, offset, length, PrimitiveInfo.FLOAT);
	}

	public static void copyIntoArray(Object srcBase, long srcOffset, double[] array, int offset, int length) {
		copyIntoArray(srcBase, srcOffset, array, offset, length, PrimitiveInfo.DOUBLE);
	}

	public static void copyIntoArray(Object srcBase, long srcOffset, char[] array, int offset, int length) {
		copyIntoArray(srcBase, srcOffset, array, offset, length, PrimitiveInfo.CHAR);
	}

	private static void copyIntoArray(Object srcBase, long srcOffset, Object array, int offset, int length, PrimitiveInfo type) {
		copyMemory(srcBase, srcOffset, array, type.arrayBaseOffset() + type.toByte(offset), type.toByte(length));
	}

	public static <T> T copyOfMemory(Object srcBase, long srcOffset, long bytes, BufferFactory<T> bufferFactory) {
		return copyOfMemory(srcBase, 0L, srcOffset, bytes, PrimitiveInfo.BYTE, bufferFactory);
	}

	static <T> T copyOfMemory(Object srcBase, long srcOffset, long offset, long length, PrimitiveInfo type, BufferFactory<T> bufferFactory) {
		return allocateAndMap(type.toByte(length), address -> {
			copyMemory(srcBase, srcOffset + type.toByte(offset), null, address, type.toByte(length));
			return bufferFactory.create(address, length, type);
		});
	}

	public static <T> T copyOfArray(byte[] array, int offset, int length, BufferFactory<T> bufferFactory) {
		return copyOfArray(array, offset, length, PrimitiveInfo.BYTE, bufferFactory);
	}

	public static <T> T copyOfArray(short[] array, int offset, int length, BufferFactory<T> bufferFactory) {
		return copyOfArray(array, offset, length, PrimitiveInfo.SHORT, bufferFactory);
	}

	public static <T> T copyOfArray(int[] array, int offset, int length, BufferFactory<T> bufferFactory) {
		return copyOfArray(array, offset, length, PrimitiveInfo.INT, bufferFactory);
	}

	public static <T> T copyOfArray(long[] array, int offset, int length, BufferFactory<T> bufferFactory) {
		return copyOfArray(array, offset, length, PrimitiveInfo.LONG, bufferFactory);
	}

	public static <T> T copyOfArray(float[] array, int offset, int length, BufferFactory<T> bufferFactory) {
		return copyOfArray(array, offset, length, PrimitiveInfo.FLOAT, bufferFactory);
	}

	public static <T> T copyOfArray(double[] array, int offset, int length, BufferFactory<T> bufferFactory) {
		return copyOfArray(array, offset, length, PrimitiveInfo.DOUBLE, bufferFactory);
	}

	public static <T> T copyOfArray(char[] array, int offset, int length, BufferFactory<T> bufferFactory) {
		return copyOfArray(array, offset, length, PrimitiveInfo.CHAR, bufferFactory);
	}

	private static <T> T copyOfArray(Object array, int offset, int length, PrimitiveInfo type, BufferFactory<T> bufferFactory) {
		return copyOfMemory(array, type.arrayBaseOffset(), offset, length, type, bufferFactory);
	}

	public static void tempMemory(long capacity, LongConsumer consumer) {
		long address = 0L;
		try {
			consumer.accept(address = allocateMemory(capacity));
		} finally {
			freeMemory(address);
		}
	}

	static <T> void tempBuffer(long capacity, PrimitiveInfo type, BufferFactory<T> bufferFactory, Consumer<T> consumer) {
		tempMemory(type.toByte(capacity), address -> consumer.accept(bufferFactory.create(address, capacity, type)));
	}

	public static <T> void tempCopyOfArray(byte[] array, BufferFactory<T> bufferFactory, Consumer<T> consumer) {
		tempCopyOfArray(array, PrimitiveInfo.BYTE, bufferFactory, consumer);
	}

	public static <T> void tempCopyOfArray(short[] array, BufferFactory<T> bufferFactory, Consumer<T> consumer) {
		tempCopyOfArray(array, PrimitiveInfo.SHORT, bufferFactory, consumer);
	}

	public static <T> void tempCopyOfArray(int[] array, BufferFactory<T> bufferFactory, Consumer<T> consumer) {
		tempCopyOfArray(array, PrimitiveInfo.INT, bufferFactory, consumer);
	}

	public static <T> void tempCopyOfArray(long[] array, BufferFactory<T> bufferFactory, Consumer<T> consumer) {
		tempCopyOfArray(array, PrimitiveInfo.LONG, bufferFactory, consumer);
	}

	public static <T> void tempCopyOfArray(float[] array, BufferFactory<T> bufferFactory, Consumer<T> consumer) {
		tempCopyOfArray(array, PrimitiveInfo.FLOAT, bufferFactory, consumer);
	}

	public static <T> void tempCopyOfArray(double[] array, BufferFactory<T> bufferFactory, Consumer<T> consumer) {
		tempCopyOfArray(array, PrimitiveInfo.DOUBLE, bufferFactory, consumer);
	}

	public static <T> void tempCopyOfArray(char[] array, BufferFactory<T> bufferFactory, Consumer<T> consumer) {
		tempCopyOfArray(array, PrimitiveInfo.CHAR, bufferFactory, consumer);
	}

	private static <T> void tempCopyOfArray(Object array, PrimitiveInfo arrayType, BufferFactory<T> bufferFactory, Consumer<T> consumer) {
		long capacity = Array.getLength(array);
		tempMemory(arrayType.toByte(capacity), address -> {
			copyMemory(array, arrayType.arrayBaseOffset(), null, address, arrayType.toByte(capacity));
			consumer.accept(bufferFactory.create(address, capacity, arrayType));
		});
	}

}
