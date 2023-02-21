package meldexun.renderlib.util;

import java.nio.Buffer;

import meldexun.matrixutil.MemoryUtil;
import meldexun.matrixutil.UnsafeUtil;

public class MemoryAccess {

	private final long address;

	public MemoryAccess(long address) {
		this.address = address;
	}

	public MemoryAccess(Buffer buffer) {
		this(MemoryUtil.getAddress(buffer));
	}

	public long getAddress() {
		return address;
	}

	public boolean getBoolean(long offset) {
		return UnsafeUtil.UNSAFE.getBoolean(null, address + offset);
	}

	public void putBoolean(long offset, boolean data) {
		UnsafeUtil.UNSAFE.putBoolean(null, address + offset, data);
	}

	public byte getByte(long offset) {
		return UnsafeUtil.UNSAFE.getByte(address + offset);
	}

	public void putByte(long offset, byte data) {
		UnsafeUtil.UNSAFE.putByte(address + offset, data);
	}

	public short getShort(long offset) {
		return UnsafeUtil.UNSAFE.getShort(address + offset);
	}

	public void putShort(long offset, short data) {
		UnsafeUtil.UNSAFE.putShort(address + offset, data);
	}

	public int getInt(long offset) {
		return UnsafeUtil.UNSAFE.getInt(address + offset);
	}

	public void putInt(long offset, int data) {
		UnsafeUtil.UNSAFE.putInt(address + offset, data);
	}

	public long getLong(long offset) {
		return UnsafeUtil.UNSAFE.getLong(address + offset);
	}

	public void putLong(long offset, long data) {
		UnsafeUtil.UNSAFE.putLong(address + offset, data);
	}

	public float getFloat(long offset) {
		return UnsafeUtil.UNSAFE.getFloat(address + offset);
	}

	public void putFloat(long offset, float data) {
		UnsafeUtil.UNSAFE.putFloat(address + offset, data);
	}

	public double getDouble(long offset) {
		return UnsafeUtil.UNSAFE.getDouble(address + offset);
	}

	public void putDouble(long offset, double data) {
		UnsafeUtil.UNSAFE.putDouble(address + offset, data);
	}

	public char getChar(long offset) {
		return UnsafeUtil.UNSAFE.getChar(address + offset);
	}

	public void putChar(long offset, char data) {
		UnsafeUtil.UNSAFE.putChar(address + offset, data);
	}

}
