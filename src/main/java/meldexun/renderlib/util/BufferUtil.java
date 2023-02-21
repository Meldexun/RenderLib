package meldexun.renderlib.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import meldexun.matrixutil.MemoryUtil;
import meldexun.matrixutil.UnsafeUtil;

public class BufferUtil {

	private static final long BYTE_ARRAY_OFFSET = UnsafeUtil.UNSAFE.arrayBaseOffset(byte[].class);
	private static final long INT_ARRAY_OFFSET = UnsafeUtil.UNSAFE.arrayBaseOffset(int[].class);
	private static final long FLOAT_ARRAY_OFFSET = UnsafeUtil.UNSAFE.arrayBaseOffset(float[].class);

	public static ByteBuffer allocate(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}

	public static IntBuffer allocateInt(int size) {
		return allocate(size << 2).asIntBuffer();
	}

	public static FloatBuffer allocateFloat(int size) {
		return allocate(size << 2).asFloatBuffer();
	}

	public static ByteBuffer buffer(byte... data) {
		return buffer(data, BYTE_ARRAY_OFFSET, data.length);
	}

	public static ByteBuffer buffer(int... data) {
		return buffer(data, INT_ARRAY_OFFSET, data.length << 2);
	}

	public static ByteBuffer buffer(float... data) {
		return buffer(data, FLOAT_ARRAY_OFFSET, data.length << 2);
	}

	public static IntBuffer bufferInt(int... data) {
		return buffer(data).asIntBuffer();
	}

	public static FloatBuffer bufferFloat(float... data) {
		return buffer(data).asFloatBuffer();
	}

	private static ByteBuffer buffer(Object src, long srcOffset, int srcLength) {
		ByteBuffer buffer = allocate(srcLength);
		UnsafeUtil.UNSAFE.copyMemory(src, srcOffset, null, MemoryUtil.getAddress(buffer), srcLength);
		return buffer;
	}

	public static ByteBuffer copy(ByteBuffer buffer) {
		return copy(MemoryUtil.getAddress(buffer) + buffer.position(), buffer.remaining());
	}

	private static ByteBuffer copy(long src, int srcLength) {
		ByteBuffer buffer = allocate(srcLength);
		UnsafeUtil.UNSAFE.copyMemory(src, MemoryUtil.getAddress(buffer), srcLength);
		return buffer;
	}

}
