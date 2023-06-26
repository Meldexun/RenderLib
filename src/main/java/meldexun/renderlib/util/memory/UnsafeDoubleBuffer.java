package meldexun.renderlib.util.memory;

import java.nio.DoubleBuffer;

public class UnsafeDoubleBuffer extends UnsafeNIOBuffer<DoubleBuffer> {

	public UnsafeDoubleBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.DOUBLE.toByte(capacity));
	}

	public long getDoubleCapacity() {
		return PrimitiveInfo.DOUBLE.fromByte(getCapacity());
	}

	@Override
	protected DoubleBuffer createBuffer() {
		return NIOBufferUtil.asDoubleBuffer(getAddress(), getDoubleCapacity());
	}

}
