package meldexun.renderlib.util.memory;

import static meldexun.matrixutil.UnsafeUtil.UNSAFE;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.function.Consumer;
import java.util.function.LongFunction;
import java.util.function.ObjLongConsumer;

public class BufferUtil {

	private static final ByteOrder NATIVE_ORDER = ByteOrder.nativeOrder();

	private static final Class<? extends ByteBuffer> BYTE_BUFFER_CLASS;
	private static final Class<? extends ShortBuffer> SHORT_BUFFER_CLASS;
	private static final Class<? extends IntBuffer> INT_BUFFER_CLASS;
	private static final Class<? extends LongBuffer> LONG_BUFFER_CLASS;
	private static final Class<? extends FloatBuffer> FLOAT_BUFFER_CLASS;
	private static final Class<? extends DoubleBuffer> DOUBLE_BUFFER_CLASS;
	private static final Class<? extends CharBuffer> CHAR_BUFFER_CLASS;

	static {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8).order(NATIVE_ORDER);

		BYTE_BUFFER_CLASS = byteBuffer.getClass();
		SHORT_BUFFER_CLASS = byteBuffer.asShortBuffer().getClass();
		INT_BUFFER_CLASS = byteBuffer.asIntBuffer().getClass();
		LONG_BUFFER_CLASS = byteBuffer.asLongBuffer().getClass();
		FLOAT_BUFFER_CLASS = byteBuffer.asFloatBuffer().getClass();
		DOUBLE_BUFFER_CLASS = byteBuffer.asDoubleBuffer().getClass();
		CHAR_BUFFER_CLASS = byteBuffer.asCharBuffer().getClass();
	}

	private static final long MARK_OFFSET = UNSAFE.objectFieldOffset(getField(Buffer.class, "mark"));
	private static final long LIMIT_OFFSET = UNSAFE.objectFieldOffset(getField(Buffer.class, "limit"));
	private static final long CAPACITY_OFFSET = UNSAFE.objectFieldOffset(getField(Buffer.class, "capacity"));
	private static final long ADDRESS_OFFSET = UNSAFE.objectFieldOffset(getField(Buffer.class, "address"));

	private static Field getField(Class<?> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static long getAddress(Buffer buffer) {
		return UNSAFE.getLong(buffer, ADDRESS_OFFSET);
	}

	public static void freeMemory(Buffer buffer) {
		if (buffer != null)
			MemoryUtil.freeMemory(getAddress(buffer));
	}

	public static ByteBuffer allocateByte(long capacity) {
		return allocate(capacity, PrimitiveInfo.BYTE, BufferUtil::asByteBuffer);
	}

	public static ShortBuffer allocateShort(long shortCapacity) {
		return allocate(shortCapacity, PrimitiveInfo.SHORT, BufferUtil::asShortBuffer);
	}

	public static IntBuffer allocateInt(long intCapacity) {
		return allocate(intCapacity, PrimitiveInfo.INT, BufferUtil::asIntBuffer);
	}

	public static LongBuffer allocateLong(long longCapacity) {
		return allocate(longCapacity, PrimitiveInfo.LONG, BufferUtil::asLongBuffer);
	}

	public static FloatBuffer allocateFloat(long floatCapacity) {
		return allocate(floatCapacity, PrimitiveInfo.FLOAT, BufferUtil::asFloatBuffer);
	}

	public static DoubleBuffer allocateDouble(long doubleCapacity) {
		return allocate(doubleCapacity, PrimitiveInfo.DOUBLE, BufferUtil::asDoubleBuffer);
	}

	public static CharBuffer allocateChar(long charCapacity) {
		return allocate(charCapacity, PrimitiveInfo.CHAR, BufferUtil::asCharBuffer);
	}

	private static <T extends Buffer> T allocate(long capacity, PrimitiveInfo type, LongLongFunction<T> bufferFactory) {
		return bufferFactory.apply(MemoryUtil.allocateMemory(type.toByte(capacity)), capacity);
	}

	public static MemoryAccess asMemoryAccess(Buffer buffer) {
		return MemoryAccess.of(getAddress(buffer));
	}

	public static ByteBuffer asByteBuffer(long address, long capacity) {
		return asBuffer(BYTE_BUFFER_CLASS, address, capacity).order(NATIVE_ORDER);
	}

	public static ShortBuffer asShortBuffer(long address, long shortCapacity) {
		return asBuffer(SHORT_BUFFER_CLASS, address, shortCapacity);
	}

	public static IntBuffer asIntBuffer(long address, long intCapacity) {
		return asBuffer(INT_BUFFER_CLASS, address, intCapacity);
	}

	public static LongBuffer asLongBuffer(long address, long longCapacity) {
		return asBuffer(LONG_BUFFER_CLASS, address, longCapacity);
	}

	public static FloatBuffer asFloatBuffer(long address, long floatCapacity) {
		return asBuffer(FLOAT_BUFFER_CLASS, address, floatCapacity);
	}

	public static DoubleBuffer asDoubleBuffer(long address, long doubleCapacity) {
		return asBuffer(DOUBLE_BUFFER_CLASS, address, doubleCapacity);
	}

	public static CharBuffer asCharBuffer(long address, long charCapacity) {
		return asBuffer(CHAR_BUFFER_CLASS, address, charCapacity);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Buffer> T asBuffer(Class<T> clazz, long address, long capacity) {
		T buffer;
		try {
			buffer = (T) UNSAFE.allocateInstance(clazz);
		} catch (InstantiationException e) {
			throw new UnsupportedOperationException(e);
		}
		UNSAFE.putInt(buffer, MARK_OFFSET, -1);
		UNSAFE.putInt(buffer, LIMIT_OFFSET, (int) capacity);
		UNSAFE.putInt(buffer, CAPACITY_OFFSET, (int) capacity);
		UNSAFE.putLong(buffer, ADDRESS_OFFSET, address);
		return buffer;
	}

	public static void tempByteBuffer(long capacity, Consumer<ByteBuffer> consumer) {
		tempBuffer(capacity, BufferUtil::allocateByte, consumer);
	}

	public static void tempShortBuffer(long shortCapacity, Consumer<ShortBuffer> consumer) {
		tempBuffer(shortCapacity, BufferUtil::allocateShort, consumer);
	}

	public static void tempIntBuffer(long intCapacity, Consumer<IntBuffer> consumer) {
		tempBuffer(intCapacity, BufferUtil::allocateInt, consumer);
	}

	public static void tempLongBuffer(long longCapacity, Consumer<LongBuffer> consumer) {
		tempBuffer(longCapacity, BufferUtil::allocateLong, consumer);
	}

	public static void tempFloatBuffer(long floatCapacity, Consumer<FloatBuffer> consumer) {
		tempBuffer(floatCapacity, BufferUtil::allocateFloat, consumer);
	}

	public static void tempDoubleBuffer(long doubleCapacity, Consumer<DoubleBuffer> consumer) {
		tempBuffer(doubleCapacity, BufferUtil::allocateDouble, consumer);
	}

	public static void tempCharBuffer(long charCapacity, Consumer<CharBuffer> consumer) {
		tempBuffer(charCapacity, BufferUtil::allocateChar, consumer);
	}

	private static <T extends Buffer> void tempBuffer(long capacity, LongFunction<T> bufferFactory, Consumer<T> consumer) {
		T buffer = null;
		try {
			buffer = bufferFactory.apply(capacity);
			consumer.accept(buffer);
		} finally {
			freeMemory(buffer);
		}
	}

	public static void tempByteBuffer(byte[] data, Consumer<ByteBuffer> consumer) {
		tempBuffer(data, BufferUtil::allocateByte, MemoryUtil::copyMemory, consumer);
	}

	public static void tempShortBuffer(short[] data, Consumer<ShortBuffer> consumer) {
		tempBuffer(data, BufferUtil::allocateShort, MemoryUtil::copyMemory, consumer);
	}

	public static void tempIntBuffer(int[] data, Consumer<IntBuffer> consumer) {
		tempBuffer(data, BufferUtil::allocateInt, MemoryUtil::copyMemory, consumer);
	}

	public static void tempLongBuffer(long[] data, Consumer<LongBuffer> consumer) {
		tempBuffer(data, BufferUtil::allocateLong, MemoryUtil::copyMemory, consumer);
	}

	public static void tempFloatBuffer(float[] data, Consumer<FloatBuffer> consumer) {
		tempBuffer(data, BufferUtil::allocateFloat, MemoryUtil::copyMemory, consumer);
	}

	public static void tempDoubleBuffer(double[] data, Consumer<DoubleBuffer> consumer) {
		tempBuffer(data, BufferUtil::allocateDouble, MemoryUtil::copyMemory, consumer);
	}

	public static void tempCharBuffer(char[] data, Consumer<CharBuffer> consumer) {
		tempBuffer(data, BufferUtil::allocateChar, MemoryUtil::copyMemory, consumer);
	}

	private static <A, T extends Buffer> void tempBuffer(A data, LongFunction<T> bufferFactory, ObjLongConsumer<A> copyFunction, Consumer<T> consumer) {
		tempBuffer(data, PrimitiveInfo.BYTE, bufferFactory, copyFunction, consumer);
	}

	private static <A, T extends Buffer> void tempBuffer(A data, PrimitiveInfo type, LongFunction<T> bufferFactory, ObjLongConsumer<A> copyFunction, Consumer<T> consumer) {
		tempBuffer(type.toByte(Array.getLength(data)), bufferFactory, ((Consumer<T>) buffer -> copyFunction.accept(data, getAddress(buffer))).andThen(consumer));
	}

}
