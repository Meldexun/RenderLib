package meldexun.renderlib.util.memory;

import java.util.function.Consumer;

public class UnsafeBufferUtil {

	public static UnsafeBuffer allocate(long byteCapacity) {
		return MemoryUtil.allocateBuffer(byteCapacity, PrimitiveInfo.BYTE, BufferFactory.UNSAFE_BUFFER);
	}

	public static UnsafeByteBuffer allocateByte(long byteCapacity) {
		return MemoryUtil.allocateBuffer(byteCapacity, PrimitiveInfo.BYTE, BufferFactory.UNSAFE_BYTE_BUFFER);
	}

	public static UnsafeShortBuffer allocateShort(long shortCapacity) {
		return MemoryUtil.allocateBuffer(shortCapacity, PrimitiveInfo.SHORT, BufferFactory.UNSAFE_SHORT_BUFFER);
	}

	public static UnsafeIntBuffer allocateInt(long intCapacity) {
		return MemoryUtil.allocateBuffer(intCapacity, PrimitiveInfo.INT, BufferFactory.UNSAFE_INT_BUFFER);
	}

	public static UnsafeLongBuffer allocateLong(long longCapacity) {
		return MemoryUtil.allocateBuffer(longCapacity, PrimitiveInfo.LONG, BufferFactory.UNSAFE_LONG_BUFFER);
	}

	public static UnsafeFloatBuffer allocateFloat(long floatCapacity) {
		return MemoryUtil.allocateBuffer(floatCapacity, PrimitiveInfo.FLOAT, BufferFactory.UNSAFE_FLOAT_BUFFER);
	}

	public static UnsafeDoubleBuffer allocateDouble(long doubleCapacity) {
		return MemoryUtil.allocateBuffer(doubleCapacity, PrimitiveInfo.DOUBLE, BufferFactory.UNSAFE_DOUBLE_BUFFER);
	}

	public static UnsafeCharBuffer allocateChar(long charCapacity) {
		return MemoryUtil.allocateBuffer(charCapacity, PrimitiveInfo.CHAR, BufferFactory.UNSAFE_CHAR_BUFFER);
	}

	public static void tempBuffer(long byteCapacity, Consumer<UnsafeBuffer> consumer) {
		MemoryUtil.tempBuffer(byteCapacity, PrimitiveInfo.BYTE, BufferFactory.UNSAFE_BUFFER, consumer);
	}

	public static void tempByteBuffer(long byteCapacity, Consumer<UnsafeByteBuffer> consumer) {
		MemoryUtil.tempBuffer(byteCapacity, PrimitiveInfo.BYTE, BufferFactory.UNSAFE_BYTE_BUFFER, consumer);
	}

	public static void tempShortBuffer(long shortCapacity, Consumer<UnsafeShortBuffer> consumer) {
		MemoryUtil.tempBuffer(shortCapacity, PrimitiveInfo.SHORT, BufferFactory.UNSAFE_SHORT_BUFFER, consumer);
	}

	public static void tempIntBuffer(long intCapacity, Consumer<UnsafeIntBuffer> consumer) {
		MemoryUtil.tempBuffer(intCapacity, PrimitiveInfo.INT, BufferFactory.UNSAFE_INT_BUFFER, consumer);
	}

	public static void tempLongBuffer(long longCapacity, Consumer<UnsafeLongBuffer> consumer) {
		MemoryUtil.tempBuffer(longCapacity, PrimitiveInfo.LONG, BufferFactory.UNSAFE_LONG_BUFFER, consumer);
	}

	public static void tempFloatBuffer(long floatCapacity, Consumer<UnsafeFloatBuffer> consumer) {
		MemoryUtil.tempBuffer(floatCapacity, PrimitiveInfo.FLOAT, BufferFactory.UNSAFE_FLOAT_BUFFER, consumer);
	}

	public static void tempDoubleBuffer(long doubleCapacity, Consumer<UnsafeDoubleBuffer> consumer) {
		MemoryUtil.tempBuffer(doubleCapacity, PrimitiveInfo.DOUBLE, BufferFactory.UNSAFE_DOUBLE_BUFFER, consumer);
	}

	public static void tempCharBuffer(long charCapacity, Consumer<UnsafeCharBuffer> consumer) {
		MemoryUtil.tempBuffer(charCapacity, PrimitiveInfo.CHAR, BufferFactory.UNSAFE_CHAR_BUFFER, consumer);
	}

}
