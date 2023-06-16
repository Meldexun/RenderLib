package meldexun.renderlib.util.memory;

import java.nio.ShortBuffer;

public class UnsafeShortBuffer extends UnsafeBufferNIO<ShortBuffer> {

	public UnsafeShortBuffer(long address, long capacity) {
		super(address, capacity << 1);
	}

	public long getShortCapacity() {
		return getCapacity() >> 1;
	}

	@Override
	protected ShortBuffer createBuffer() {
		return NIOBufferUtil.asShortBuffer(getAddress(), getShortCapacity());
	}

}
