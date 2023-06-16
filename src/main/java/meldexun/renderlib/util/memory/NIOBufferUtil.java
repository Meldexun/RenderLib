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

public class NIOBufferUtil {

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
			BufferUtil.freeMemory(getAddress(buffer));
	}

	public static ByteBuffer allocateByte(long capacity) {
		return asByteBuffer(BufferUtil.allocateMemory(capacity), capacity);
	}

	public static ShortBuffer allocateShort(long capacity) {
		return asShortBuffer(BufferUtil.allocateMemory(capacity << 1), capacity);
	}

	public static IntBuffer allocateInt(long capacity) {
		return asIntBuffer(BufferUtil.allocateMemory(capacity << 2), capacity);
	}

	public static LongBuffer allocateLong(long capacity) {
		return asLongBuffer(BufferUtil.allocateMemory(capacity << 3), capacity);
	}

	public static FloatBuffer allocateFloat(long capacity) {
		return asFloatBuffer(BufferUtil.allocateMemory(capacity << 2), capacity);
	}

	public static DoubleBuffer allocateDouble(long capacity) {
		return asDoubleBuffer(BufferUtil.allocateMemory(capacity << 3), capacity);
	}

	public static CharBuffer allocateChar(long capacity) {
		return asCharBuffer(BufferUtil.allocateMemory(capacity << 1), capacity);
	}

	public static ByteBuffer asByteBuffer(long address, long capacity) {
		return asBuffer(BYTE_BUFFER_CLASS, address, capacity).order(NATIVE_ORDER);
	}

	public static ShortBuffer asShortBuffer(long address, long capacity) {
		return asBuffer(SHORT_BUFFER_CLASS, address, capacity);
	}

	public static IntBuffer asIntBuffer(long address, long capacity) {
		return asBuffer(INT_BUFFER_CLASS, address, capacity);
	}

	public static LongBuffer asLongBuffer(long address, long capacity) {
		return asBuffer(LONG_BUFFER_CLASS, address, capacity);
	}

	public static FloatBuffer asFloatBuffer(long address, long capacity) {
		return asBuffer(FLOAT_BUFFER_CLASS, address, capacity);
	}

	public static DoubleBuffer asDoubleBuffer(long address, long capacity) {
		return asBuffer(DOUBLE_BUFFER_CLASS, address, capacity);
	}

	public static CharBuffer asCharBuffer(long address, long capacity) {
		return asBuffer(CHAR_BUFFER_CLASS, address, capacity);
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
		tempBuffer(capacity, NIOBufferUtil::allocateByte, consumer);
	}

	public static void tempShortBuffer(long capacity, Consumer<ShortBuffer> consumer) {
		tempBuffer(capacity, NIOBufferUtil::allocateShort, consumer);
	}

	public static void tempIntBuffer(long capacity, Consumer<IntBuffer> consumer) {
		tempBuffer(capacity, NIOBufferUtil::allocateInt, consumer);
	}

	public static void tempLongBuffer(long capacity, Consumer<LongBuffer> consumer) {
		tempBuffer(capacity, NIOBufferUtil::allocateLong, consumer);
	}

	public static void tempFloatBuffer(long capacity, Consumer<FloatBuffer> consumer) {
		tempBuffer(capacity, NIOBufferUtil::allocateFloat, consumer);
	}

	public static void tempDoubleBuffer(long capacity, Consumer<DoubleBuffer> consumer) {
		tempBuffer(capacity, NIOBufferUtil::allocateDouble, consumer);
	}

	public static void tempCharBuffer(long capacity, Consumer<CharBuffer> consumer) {
		tempBuffer(capacity, NIOBufferUtil::allocateChar, consumer);
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
		tempBuffer(data, NIOBufferUtil::allocateByte, BufferUtil::copyMemory, consumer);
	}

	public static void tempShortBuffer(short[] data, Consumer<ShortBuffer> consumer) {
		tempBuffer(data, NIOBufferUtil::allocateShort, BufferUtil::copyMemory, consumer);
	}

	public static void tempIntBuffer(int[] data, Consumer<IntBuffer> consumer) {
		tempBuffer(data, NIOBufferUtil::allocateInt, BufferUtil::copyMemory, consumer);
	}

	public static void tempLongBuffer(long[] data, Consumer<LongBuffer> consumer) {
		tempBuffer(data, NIOBufferUtil::allocateLong, BufferUtil::copyMemory, consumer);
	}

	public static void tempFloatBuffer(float[] data, Consumer<FloatBuffer> consumer) {
		tempBuffer(data, NIOBufferUtil::allocateFloat, BufferUtil::copyMemory, consumer);
	}

	public static void tempDoubleBuffer(double[] data, Consumer<DoubleBuffer> consumer) {
		tempBuffer(data, NIOBufferUtil::allocateDouble, BufferUtil::copyMemory, consumer);
	}

	public static void tempCharBuffer(char[] data, Consumer<CharBuffer> consumer) {
		tempBuffer(data, NIOBufferUtil::allocateChar, BufferUtil::copyMemory, consumer);
	}

	private static <A, T extends Buffer> void tempBuffer(A data, LongFunction<T> bufferFactory, ObjLongConsumer<A> copyFunction, Consumer<T> consumer) {
		tempBuffer(Array.getLength(data), bufferFactory, ((Consumer<T>) buffer -> copyFunction.accept(data, getAddress(buffer))).andThen(consumer));
	}

}
