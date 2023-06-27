package meldexun.renderlib.util.memory;

import static meldexun.matrixutil.UnsafeUtil.UNSAFE;

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
			MemoryUtil.freeMemory(getAddress(buffer));
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

	public static ByteBuffer allocateByte(long capacity) {
		return MemoryUtil.allocateBuffer(capacity, PrimitiveInfo.BYTE, BufferFactory.NIO_BYTE_BUFFER);
	}

	public static ShortBuffer allocateShort(long shortCapacity) {
		return MemoryUtil.allocateBuffer(shortCapacity, PrimitiveInfo.SHORT, BufferFactory.NIO_SHORT_BUFFER);
	}

	public static IntBuffer allocateInt(long intCapacity) {
		return MemoryUtil.allocateBuffer(intCapacity, PrimitiveInfo.INT, BufferFactory.NIO_INT_BUFFER);
	}

	public static LongBuffer allocateLong(long longCapacity) {
		return MemoryUtil.allocateBuffer(longCapacity, PrimitiveInfo.LONG, BufferFactory.NIO_LONG_BUFFER);
	}

	public static FloatBuffer allocateFloat(long floatCapacity) {
		return MemoryUtil.allocateBuffer(floatCapacity, PrimitiveInfo.FLOAT, BufferFactory.NIO_FLOAT_BUFFER);
	}

	public static DoubleBuffer allocateDouble(long doubleCapacity) {
		return MemoryUtil.allocateBuffer(doubleCapacity, PrimitiveInfo.DOUBLE, BufferFactory.NIO_DOUBLE_BUFFER);
	}

	public static CharBuffer allocateChar(long charCapacity) {
		return MemoryUtil.allocateBuffer(charCapacity, PrimitiveInfo.CHAR, BufferFactory.NIO_CHAR_BUFFER);
	}

	public static UnsafeByteBuffer copyAsUnsafeBuffer(ByteBuffer buffer) {
		return copyAsUnsafeBuffer(buffer, PrimitiveInfo.BYTE, BufferFactory.UNSAFE_BYTE_BUFFER);
	}

	public static UnsafeShortBuffer copyAsUnsafeBuffer(ShortBuffer buffer) {
		return copyAsUnsafeBuffer(buffer, PrimitiveInfo.SHORT, BufferFactory.UNSAFE_SHORT_BUFFER);
	}

	public static UnsafeIntBuffer copyAsUnsafeBuffer(IntBuffer buffer) {
		return copyAsUnsafeBuffer(buffer, PrimitiveInfo.INT, BufferFactory.UNSAFE_INT_BUFFER);
	}

	public static UnsafeLongBuffer copyAsUnsafeBuffer(LongBuffer buffer) {
		return copyAsUnsafeBuffer(buffer, PrimitiveInfo.LONG, BufferFactory.UNSAFE_LONG_BUFFER);
	}

	public static UnsafeFloatBuffer copyAsUnsafeBuffer(FloatBuffer buffer) {
		return copyAsUnsafeBuffer(buffer, PrimitiveInfo.FLOAT, BufferFactory.UNSAFE_FLOAT_BUFFER);
	}

	public static UnsafeDoubleBuffer copyAsUnsafeBuffer(DoubleBuffer buffer) {
		return copyAsUnsafeBuffer(buffer, PrimitiveInfo.DOUBLE, BufferFactory.UNSAFE_DOUBLE_BUFFER);
	}

	public static UnsafeCharBuffer copyAsUnsafeBuffer(CharBuffer buffer) {
		return copyAsUnsafeBuffer(buffer, PrimitiveInfo.CHAR, BufferFactory.UNSAFE_CHAR_BUFFER);
	}

	private static <T> T copyAsUnsafeBuffer(Buffer buffer, PrimitiveInfo type, BufferFactory<T> bufferFactory) {
		return MemoryUtil.copyOfMemory(null, getAddress(buffer), buffer.position(), buffer.remaining(), type, bufferFactory);
	}

	public static void tempByteBuffer(byte[] array, Consumer<ByteBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_BYTE_BUFFER, consumer);
	}

	public static void tempByteBuffer(short[] array, Consumer<ByteBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_BYTE_BUFFER, consumer);
	}

	public static void tempByteBuffer(int[] array, Consumer<ByteBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_BYTE_BUFFER, consumer);
	}

	public static void tempByteBuffer(long[] array, Consumer<ByteBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_BYTE_BUFFER, consumer);
	}

	public static void tempByteBuffer(float[] array, Consumer<ByteBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_BYTE_BUFFER, consumer);
	}

	public static void tempByteBuffer(double[] array, Consumer<ByteBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_BYTE_BUFFER, consumer);
	}

	public static void tempByteBuffer(char[] array, Consumer<ByteBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_BYTE_BUFFER, consumer);
	}

	public static void tempShortBuffer(short[] array, Consumer<ShortBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_SHORT_BUFFER, consumer);
	}

	public static void tempIntBuffer(int[] array, Consumer<IntBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_INT_BUFFER, consumer);
	}

	public static void tempLongBuffer(long[] array, Consumer<LongBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_LONG_BUFFER, consumer);
	}

	public static void tempFloatBuffer(float[] array, Consumer<FloatBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_FLOAT_BUFFER, consumer);
	}

	public static void tempDoubleBuffer(double[] array, Consumer<DoubleBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_DOUBLE_BUFFER, consumer);
	}

	public static void tempCharBuffer(char[] array, Consumer<CharBuffer> consumer) {
		MemoryUtil.tempCopyOfArray(array, BufferFactory.NIO_CHAR_BUFFER, consumer);
	}

}
