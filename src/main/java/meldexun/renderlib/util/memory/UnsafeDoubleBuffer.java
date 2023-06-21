package meldexun.renderlib.util.memory;

import java.nio.DoubleBuffer;

public class UnsafeDoubleBuffer extends UnsafeBufferNIO<DoubleBuffer> {

	public UnsafeDoubleBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.DOUBLE.toByte(capacity));
	}

	public long getDoubleCapacity() {
		return PrimitiveInfo.DOUBLE.fromByte(getCapacity());
	}

	@Override
	protected DoubleBuffer createBuffer() {
		return BufferUtil.asDoubleBuffer(getAddress(), getDoubleCapacity());
	}

}
