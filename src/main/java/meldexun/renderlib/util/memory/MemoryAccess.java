package meldexun.renderlib.util.memory;

import static meldexun.matrixutil.UnsafeUtil.UNSAFE;

public interface MemoryAccess {

	long getAddress();

	default boolean getBoolean(long offset) {
		return UNSAFE.getBoolean(null, getAddress() + offset);
	}

	default void putBoolean(long offset, boolean data) {
		UNSAFE.putBoolean(null, getAddress() + offset, data);
	}

	default byte getByte(long offset) {
		return UNSAFE.getByte(getAddress() + offset);
	}

	default void putByte(long offset, byte data) {
		UNSAFE.putByte(getAddress() + offset, data);
	}

	default short getShort(long offset) {
		return UNSAFE.getShort(getAddress() + offset);
	}

	default void putShort(long offset, short data) {
		UNSAFE.putShort(getAddress() + offset, data);
	}

	default int getInt(long offset) {
		return UNSAFE.getInt(getAddress() + offset);
	}

	default void putInt(long offset, int data) {
		UNSAFE.putInt(getAddress() + offset, data);
	}

	default long getLong(long offset) {
		return UNSAFE.getLong(getAddress() + offset);
	}

	default void putLong(long offset, long data) {
		UNSAFE.putLong(getAddress() + offset, data);
	}

	default float getFloat(long offset) {
		return UNSAFE.getFloat(getAddress() + offset);
	}

	default void putFloat(long offset, float data) {
		UNSAFE.putFloat(getAddress() + offset, data);
	}

	default double getDouble(long offset) {
		return UNSAFE.getDouble(getAddress() + offset);
	}

	default void putDouble(long offset, double data) {
		UNSAFE.putDouble(getAddress() + offset, data);
	}

	default char getChar(long offset) {
		return UNSAFE.getChar(getAddress() + offset);
	}

	default void putChar(long offset, char data) {
		UNSAFE.putChar(getAddress() + offset, data);
	}

	default void copyMemoryFrom(long srcAddress, long destOffset, long bytes) {
		UNSAFE.copyMemory(null, srcAddress, null, this.getAddress() + destOffset, bytes);
	}

	default void copyMemoryFrom(Object srcBase, long srcOffset, long destOffset, long bytes) {
		UNSAFE.copyMemory(srcBase, srcOffset, null, this.getAddress() + destOffset, bytes);
	}

	default void copyMemoryFrom(MemoryAccess srcBase, long srcOffset, long destOffset, long bytes) {
		UNSAFE.copyMemory(null, srcBase.getAddress() + srcOffset, null, this.getAddress() + destOffset, bytes);
	}

	default void copyMemoryInto(long srcOffset, long destAddress, long bytes) {
		UNSAFE.copyMemory(null, this.getAddress() + srcOffset, null, destAddress, bytes);
	}

	default void copyMemoryInto(long srcOffset, Object destBase, long destOffset, long bytes) {
		UNSAFE.copyMemory(null, this.getAddress() + srcOffset, destBase, destOffset, bytes);
	}

	default void copyMemoryInto(long srcOffset, MemoryAccess destBase, long destOffset, long bytes) {
		UNSAFE.copyMemory(null, this.getAddress() + srcOffset, null, destBase.getAddress() + destOffset, bytes);
	}

}
