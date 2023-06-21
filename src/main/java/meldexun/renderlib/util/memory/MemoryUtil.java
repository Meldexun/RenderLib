package meldexun.renderlib.util.memory;

import static meldexun.matrixutil.UnsafeUtil.UNSAFE;
import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_DOUBLE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_FLOAT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_LONG_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_SHORT_BASE_OFFSET;

import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.ObjLongConsumer;

public class MemoryUtil {

	public static long allocateMemory(long capacity) {
		return UNSAFE.allocateMemory(capacity);
	}

	public static void freeMemory(long address) {
		UNSAFE.freeMemory(address);
	}

	public static UnsafeBuffer allocate(long capacity) {
		return new UnsafeBuffer(allocateMemory(capacity), capacity);
	}

	public static UnsafeByteBuffer allocateByte(long capacity) {
		return new UnsafeByteBuffer(allocateMemory(capacity), capacity);
	}

	public static UnsafeShortBuffer allocateShort(long capacity) {
		return new UnsafeShortBuffer(allocateMemory(capacity << 1), capacity);
	}

	public static UnsafeIntBuffer allocateInt(long capacity) {
		return new UnsafeIntBuffer(allocateMemory(capacity << 2), capacity);
	}

	public static UnsafeLongBuffer allocateLong(long capacity) {
		return new UnsafeLongBuffer(allocateMemory(capacity << 3), capacity);
	}

	public static UnsafeFloatBuffer allocateFloat(long capacity) {
		return new UnsafeFloatBuffer(allocateMemory(capacity << 2), capacity);
	}

	public static UnsafeDoubleBuffer allocateDouble(long capacity) {
		return new UnsafeDoubleBuffer(allocateMemory(capacity << 3), capacity);
	}

	public static UnsafeCharBuffer allocateChar(long capacity) {
		return new UnsafeCharBuffer(allocateMemory(capacity << 1), capacity);
	}

	public static void tempBuffer(long capacity, LongConsumer consumer) {
		long address = 0L;
		try {
			address = allocateMemory(capacity);
			consumer.accept(address);
		} finally {
			freeMemory(address);
		}
	}

	public static void tempBuffer(long capacity, Consumer<UnsafeBuffer> consumer) {
		tempBuffer(capacity, MemoryUtil::allocate, consumer);
	}

	public static void tempByteBuffer(long capacity, Consumer<UnsafeByteBuffer> consumer) {
		tempBuffer(capacity, MemoryUtil::allocateByte, consumer);
	}

	public static void tempShortBuffer(long capacity, Consumer<UnsafeShortBuffer> consumer) {
		tempBuffer(capacity, MemoryUtil::allocateShort, consumer);
	}

	public static void tempIntBuffer(long capacity, Consumer<UnsafeIntBuffer> consumer) {
		tempBuffer(capacity, MemoryUtil::allocateInt, consumer);
	}

	public static void tempLongBuffer(long capacity, Consumer<UnsafeLongBuffer> consumer) {
		tempBuffer(capacity, MemoryUtil::allocateLong, consumer);
	}

	public static void tempFloatBuffer(long capacity, Consumer<UnsafeFloatBuffer> consumer) {
		tempBuffer(capacity, MemoryUtil::allocateFloat, consumer);
	}

	public static void tempDoubleBuffer(long capacity, Consumer<UnsafeDoubleBuffer> consumer) {
		tempBuffer(capacity, MemoryUtil::allocateDouble, consumer);
	}

	public static void tempCharBuffer(long capacity, Consumer<UnsafeCharBuffer> consumer) {
		tempBuffer(capacity, MemoryUtil::allocateChar, consumer);
	}

	private static <T extends UnsafeBuffer> void tempBuffer(long capacity, LongFunction<T> bufferFactory, Consumer<T> consumer) {
		try (T buffer = bufferFactory.apply(capacity)) {
			consumer.accept(buffer);
		}
	}

	public static void tempByteBuffer(byte[] data, Consumer<UnsafeByteBuffer> consumer) {
		tempBuffer(data, MemoryUtil::allocateByte, MemoryUtil::copyMemory, consumer);
	}

	public static void tempShortBuffer(short[] data, Consumer<UnsafeShortBuffer> consumer) {
		tempBuffer(data, MemoryUtil::allocateShort, MemoryUtil::copyMemory, consumer);
	}

	public static void tempIntBuffer(int[] data, Consumer<UnsafeIntBuffer> consumer) {
		tempBuffer(data, MemoryUtil::allocateInt, MemoryUtil::copyMemory, consumer);
	}

	public static void tempLongBuffer(long[] data, Consumer<UnsafeLongBuffer> consumer) {
		tempBuffer(data, MemoryUtil::allocateLong, MemoryUtil::copyMemory, consumer);
	}

	public static void tempFloatBuffer(float[] data, Consumer<UnsafeFloatBuffer> consumer) {
		tempBuffer(data, MemoryUtil::allocateFloat, MemoryUtil::copyMemory, consumer);
	}

	public static void tempDoubleBuffer(double[] data, Consumer<UnsafeDoubleBuffer> consumer) {
		tempBuffer(data, MemoryUtil::allocateDouble, MemoryUtil::copyMemory, consumer);
	}

	public static void tempCharBuffer(char[] data, Consumer<UnsafeCharBuffer> consumer) {
		tempBuffer(data, MemoryUtil::allocateChar, MemoryUtil::copyMemory, consumer);
	}

	private static <A, T extends UnsafeBuffer> void tempBuffer(A data, LongFunction<T> bufferFactory, ObjLongConsumer<A> copyFunction, Consumer<T> consumer) {
		tempBuffer(Array.getLength(data), bufferFactory, ((Consumer<T>) buffer -> copyFunction.accept(data, buffer.getAddress())).andThen(consumer));
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

	public static void copyMemory(byte[] src, long destAddress) {
		copyMemory(src, 0, null, destAddress, src.length);
	}

	public static void copyMemory(short[] src, long destAddress) {
		copyMemory(src, 0, null, destAddress, src.length);
	}

	public static void copyMemory(int[] src, long destAddress) {
		copyMemory(src, 0, null, destAddress, src.length);
	}

	public static void copyMemory(long[] src, long destAddress) {
		copyMemory(src, 0, null, destAddress, src.length);
	}

	public static void copyMemory(float[] src, long destAddress) {
		copyMemory(src, 0, null, destAddress, src.length);
	}

	public static void copyMemory(double[] src, long destAddress) {
		copyMemory(src, 0, null, destAddress, src.length);
	}

	public static void copyMemory(char[] src, long destAddress) {
		copyMemory(src, 0, null, destAddress, src.length);
	}

	public static void copyMemory(byte[] src, int srcOffset, Object destBase, long destOffset, int srcLength) {
		copyMemory(src, ARRAY_BYTE_BASE_OFFSET + srcOffset, destBase, destOffset, srcLength);
	}

	public static void copyMemory(short[] src, int srcOffset, Object destBase, long destOffset, int srcLength) {
		copyMemory(src, ARRAY_SHORT_BASE_OFFSET + (srcOffset << 1), destBase, destOffset, srcLength << 1);
	}

	public static void copyMemory(int[] src, int srcOffset, Object destBase, long destOffset, int srcLength) {
		copyMemory(src, ARRAY_INT_BASE_OFFSET + (srcOffset << 2), destBase, destOffset, srcLength << 2);
	}

	public static void copyMemory(long[] src, int srcOffset, Object destBase, long destOffset, int srcLength) {
		copyMemory(src, ARRAY_LONG_BASE_OFFSET + (srcOffset << 3), destBase, destOffset, srcLength << 3);
	}

	public static void copyMemory(float[] src, int srcOffset, Object destBase, long destOffset, int srcLength) {
		copyMemory(src, ARRAY_FLOAT_BASE_OFFSET + (srcOffset << 2), destBase, destOffset, srcLength << 2);
	}

	public static void copyMemory(double[] src, int srcOffset, Object destBase, long destOffset, int srcLength) {
		copyMemory(src, ARRAY_DOUBLE_BASE_OFFSET + (srcOffset << 3), destBase, destOffset, srcLength << 3);
	}

	public static void copyMemory(char[] src, int srcOffset, Object destBase, long destOffset, int srcLength) {
		copyMemory(src, ARRAY_CHAR_BASE_OFFSET + (srcOffset << 1), destBase, destOffset, srcLength << 1);
	}

	public static void copyMemory(long srcAddress, byte[] dest) {
		copyMemory(null, srcAddress, dest, 0, dest.length);
	}

	public static void copyMemory(long srcAddress, short[] dest) {
		copyMemory(null, srcAddress, dest, 0, dest.length);
	}

	public static void copyMemory(long srcAddress, int[] dest) {
		copyMemory(null, srcAddress, dest, 0, dest.length);
	}

	public static void copyMemory(long srcAddress, long[] dest) {
		copyMemory(null, srcAddress, dest, 0, dest.length);
	}

	public static void copyMemory(long srcAddress, float[] dest) {
		copyMemory(null, srcAddress, dest, 0, dest.length);
	}

	public static void copyMemory(long srcAddress, double[] dest) {
		copyMemory(null, srcAddress, dest, 0, dest.length);
	}

	public static void copyMemory(long srcAddress, char[] dest) {
		copyMemory(null, srcAddress, dest, 0, dest.length);
	}

	public static void copyMemory(Object srcBase, long srcOffset, byte[] dest, int destOffset, int destLength) {
		copyMemory(srcBase, srcOffset, dest, ARRAY_BYTE_BASE_OFFSET + destOffset, destLength);
	}

	public static void copyMemory(Object srcBase, long srcOffset, short[] dest, int destOffset, int destLength) {
		copyMemory(srcBase, srcOffset, dest, ARRAY_SHORT_BASE_OFFSET + (destOffset << 1), destLength << 1);
	}

	public static void copyMemory(Object srcBase, long srcOffset, int[] dest, int destOffset, int destLength) {
		copyMemory(srcBase, srcOffset, dest, ARRAY_INT_BASE_OFFSET + (destOffset << 2), destLength << 2);
	}

	public static void copyMemory(Object srcBase, long srcOffset, long[] dest, int destOffset, int destLength) {
		copyMemory(srcBase, srcOffset, dest, ARRAY_LONG_BASE_OFFSET + (destOffset << 3), destLength << 3);
	}

	public static void copyMemory(Object srcBase, long srcOffset, float[] dest, int destOffset, int destLength) {
		copyMemory(srcBase, srcOffset, dest, ARRAY_FLOAT_BASE_OFFSET + (destOffset << 2), destLength << 2);
	}

	public static void copyMemory(Object srcBase, long srcOffset, double[] dest, int destOffset, int destLength) {
		copyMemory(srcBase, srcOffset, dest, ARRAY_DOUBLE_BASE_OFFSET + (destOffset << 3), destLength << 3);
	}

	public static void copyMemory(Object srcBase, long srcOffset, char[] dest, int destOffset, int destLength) {
		copyMemory(srcBase, srcOffset, dest, ARRAY_CHAR_BASE_OFFSET + (destOffset << 1), destLength << 1);
	}

}
