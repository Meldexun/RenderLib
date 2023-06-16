package meldexun.renderlib.util.memory;

import java.nio.DoubleBuffer;

public class UnsafeDoubleBuffer extends UnsafeBufferNIO<DoubleBuffer> {

	public UnsafeDoubleBuffer(long address, long capacity) {
		super(address, capacity << 3);
	}

	public long getDoubleCapacity() {
		return getCapacity() >> 3;
	}

	@Override
	protected DoubleBuffer createBuffer() {
		return NIOBufferUtil.asDoubleBuffer(getAddress(), getDoubleCapacity());
	}

}
