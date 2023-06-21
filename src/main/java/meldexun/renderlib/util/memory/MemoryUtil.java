package meldexun.renderlib.util.memory;

import static meldexun.matrixutil.UnsafeUtil.UNSAFE;

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
		return allocate(capacity, PrimitiveInfo.BYTE, UnsafeBuffer::new);
	}

	public static UnsafeByteBuffer allocateByte(long capacity) {
		return allocate(capacity, PrimitiveInfo.BYTE, UnsafeByteBuffer::new);
	}

	public static UnsafeShortBuffer allocateShort(long shortCapacity) {
		return allocate(shortCapacity, PrimitiveInfo.SHORT, UnsafeShortBuffer::new);
	}

	public static UnsafeIntBuffer allocateInt(long intCapacity) {
		return allocate(intCapacity, PrimitiveInfo.INT, UnsafeIntBuffer::new);
	}

	public static UnsafeLongBuffer allocateLong(long longCapacity) {
		return allocate(longCapacity, PrimitiveInfo.LONG, UnsafeLongBuffer::new);
	}

	public static UnsafeFloatBuffer allocateFloat(long floatCapacity) {
		return allocate(floatCapacity, PrimitiveInfo.FLOAT, UnsafeFloatBuffer::new);
	}

	public static UnsafeDoubleBuffer allocateDouble(long doubleCapacity) {
		return allocate(doubleCapacity, PrimitiveInfo.DOUBLE, UnsafeDoubleBuffer::new);
	}

	public static UnsafeCharBuffer allocateChar(long charCapacity) {
		return allocate(charCapacity, PrimitiveInfo.CHAR, UnsafeCharBuffer::new);
	}

	private static <T extends UnsafeBuffer> T allocate(long capacity, PrimitiveInfo type, LongLongFunction<T> bufferFactory) {
		return bufferFactory.apply(allocateMemory(type.toByte(capacity)), capacity);
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

	public static void tempShortBuffer(long shortCapacity, Consumer<UnsafeShortBuffer> consumer) {
		tempBuffer(shortCapacity, MemoryUtil::allocateShort, consumer);
	}

	public static void tempIntBuffer(long intCapacity, Consumer<UnsafeIntBuffer> consumer) {
		tempBuffer(intCapacity, MemoryUtil::allocateInt, consumer);
	}

	public static void tempLongBuffer(long longCapacity, Consumer<UnsafeLongBuffer> consumer) {
		tempBuffer(longCapacity, MemoryUtil::allocateLong, consumer);
	}

	public static void tempFloatBuffer(long floatCapacity, Consumer<UnsafeFloatBuffer> consumer) {
		tempBuffer(floatCapacity, MemoryUtil::allocateFloat, consumer);
	}

	public static void tempDoubleBuffer(long doubelCapacity, Consumer<UnsafeDoubleBuffer> consumer) {
		tempBuffer(doubelCapacity, MemoryUtil::allocateDouble, consumer);
	}

	public static void tempCharBuffer(long charCapacity, Consumer<UnsafeCharBuffer> consumer) {
		tempBuffer(charCapacity, MemoryUtil::allocateChar, consumer);
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

	public static void copyMemory(byte[] src, int srcOffset, Object destBase, long destOffset, int bytes) {
		copyMemory(src, srcOffset, destBase, destOffset, bytes, PrimitiveInfo.BYTE);
	}

	public static void copyMemory(short[] src, int srcOffset, Object destBase, long destOffset, int shorts) {
		copyMemory(src, srcOffset, destBase, destOffset, shorts, PrimitiveInfo.SHORT);
	}

	public static void copyMemory(int[] src, int srcOffset, Object destBase, long destOffset, int ints) {
		copyMemory(src, srcOffset, destBase, destOffset, ints, PrimitiveInfo.INT);
	}

	public static void copyMemory(long[] src, int srcOffset, Object destBase, long destOffset, int longs) {
		copyMemory(src, srcOffset, destBase, destOffset, longs, PrimitiveInfo.LONG);
	}

	public static void copyMemory(float[] src, int srcOffset, Object destBase, long destOffset, int floats) {
		copyMemory(src, srcOffset, destBase, destOffset, floats, PrimitiveInfo.FLOAT);
	}

	public static void copyMemory(double[] src, int srcOffset, Object destBase, long destOffset, int doubles) {
		copyMemory(src, srcOffset, destBase, destOffset, doubles, PrimitiveInfo.DOUBLE);
	}

	public static void copyMemory(char[] src, int srcOffset, Object destBase, long destOffset, int chars) {
		copyMemory(src, srcOffset, destBase, destOffset, chars, PrimitiveInfo.CHAR);
	}

	private static <A> void copyMemory(A src, int srcOffset, Object destBase, long destOffset, int srcLength, PrimitiveInfo type) {
		copyMemory(src, type.arrayBaseOffset() + type.toByte(srcOffset), destBase, destOffset, type.toByte(srcLength));
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

	public static void copyMemory(Object srcBase, long srcOffset, byte[] dest, int destOffset, int bytes) {
		copyMemory(srcBase, srcOffset, dest, destOffset, bytes, PrimitiveInfo.BYTE);
	}

	public static void copyMemory(Object srcBase, long srcOffset, short[] dest, int destOffset, int shorts) {
		copyMemory(srcBase, srcOffset, dest, destOffset, shorts, PrimitiveInfo.SHORT);
	}

	public static void copyMemory(Object srcBase, long srcOffset, int[] dest, int destOffset, int ints) {
		copyMemory(srcBase, srcOffset, dest, destOffset, ints, PrimitiveInfo.INT);
	}

	public static void copyMemory(Object srcBase, long srcOffset, long[] dest, int destOffset, int longs) {
		copyMemory(srcBase, srcOffset, dest, destOffset, longs, PrimitiveInfo.LONG);
	}

	public static void copyMemory(Object srcBase, long srcOffset, float[] dest, int destOffset, int floats) {
		copyMemory(srcBase, srcOffset, dest, destOffset, floats, PrimitiveInfo.FLOAT);
	}

	public static void copyMemory(Object srcBase, long srcOffset, double[] dest, int destOffset, int doubles) {
		copyMemory(srcBase, srcOffset, dest, destOffset, doubles, PrimitiveInfo.DOUBLE);
	}

	public static void copyMemory(Object srcBase, long srcOffset, char[] dest, int destOffset, int chars) {
		copyMemory(srcBase, srcOffset, dest, destOffset, chars, PrimitiveInfo.CHAR);
	}

	private static <A> void copyMemory(Object srcBase, long srcOffset, A dest, int destOffset, int destLength, PrimitiveInfo type) {
		copyMemory(srcBase, srcOffset, dest, type.arrayBaseOffset() + type.toByte(destOffset), type.toByte(destLength));
	}

}
