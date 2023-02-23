package meldexun.renderlib.util;

import java.nio.Buffer;

import meldexun.matrixutil.MemoryUtil;

public class UnsafeBuffer<T extends Buffer> implements MemoryAccess {

	private final T buffer;
	private final long address;

	public UnsafeBuffer(T buffer) {
		this.buffer = buffer;
		this.address = MemoryUtil.getAddress(buffer);
	}

	public T getBuffer() {
		return buffer;
	}

	@Override
	public long getAddress() {
		return address;
	}

}
