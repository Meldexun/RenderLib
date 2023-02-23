package meldexun.renderlib.util;

import meldexun.matrixutil.UnsafeUtil;

public interface MemoryAccess {

	long getAddress();

	default boolean getBoolean(long offset) {
		return UnsafeUtil.UNSAFE.getBoolean(null, getAddress() + offset);
	}

	default void putBoolean(long offset, boolean data) {
		UnsafeUtil.UNSAFE.putBoolean(null, getAddress() + offset, data);
	}

	default byte getByte(long offset) {
		return UnsafeUtil.UNSAFE.getByte(getAddress() + offset);
	}

	default void putByte(long offset, byte data) {
		UnsafeUtil.UNSAFE.putByte(getAddress() + offset, data);
	}

	default short getShort(long offset) {
		return UnsafeUtil.UNSAFE.getShort(getAddress() + offset);
	}

	default void putShort(long offset, short data) {
		UnsafeUtil.UNSAFE.putShort(getAddress() + offset, data);
	}

	default int getInt(long offset) {
		return UnsafeUtil.UNSAFE.getInt(getAddress() + offset);
	}

	default void putInt(long offset, int data) {
		UnsafeUtil.UNSAFE.putInt(getAddress() + offset, data);
	}

	default long getLong(long offset) {
		return UnsafeUtil.UNSAFE.getLong(getAddress() + offset);
	}

	default void putLong(long offset, long data) {
		UnsafeUtil.UNSAFE.putLong(getAddress() + offset, data);
	}

	default float getFloat(long offset) {
		return UnsafeUtil.UNSAFE.getFloat(getAddress() + offset);
	}

	default void putFloat(long offset, float data) {
		UnsafeUtil.UNSAFE.putFloat(getAddress() + offset, data);
	}

	default double getDouble(long offset) {
		return UnsafeUtil.UNSAFE.getDouble(getAddress() + offset);
	}

	default void putDouble(long offset, double data) {
		UnsafeUtil.UNSAFE.putDouble(getAddress() + offset, data);
	}

	default char getChar(long offset) {
		return UnsafeUtil.UNSAFE.getChar(getAddress() + offset);
	}

	default void putChar(long offset, char data) {
		UnsafeUtil.UNSAFE.putChar(getAddress() + offset, data);
	}

	static void copyMemory(MemoryAccess src, long srcOffset, MemoryAccess dest, long destOffset, long bytes) {
		UnsafeUtil.UNSAFE.copyMemory(src.getAddress() + srcOffset, dest.getAddress() + destOffset, bytes);
	}

}
