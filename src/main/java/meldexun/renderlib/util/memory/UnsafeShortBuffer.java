package meldexun.renderlib.util.memory;

import java.nio.ShortBuffer;

public class UnsafeShortBuffer extends UnsafeNIOBuffer<ShortBuffer> {

	public UnsafeShortBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.SHORT.toByte(capacity));
	}

	public long getShortCapacity() {
		return PrimitiveInfo.SHORT.fromByte(getCapacity());
	}

	@Override
	protected ShortBuffer createBuffer() {
		return NIOBufferUtil.asShortBuffer(getAddress(), getShortCapacity());
	}

}
