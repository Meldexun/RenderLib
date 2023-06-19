package meldexun.renderlib.util.memory;

import java.nio.LongBuffer;

public class UnsafeLongBuffer extends UnsafeBufferNIO<LongBuffer> {

	public UnsafeLongBuffer(long address, long capacity) {
		super(address, capacity << 3);
	}

	public long getLongCapacity() {
		return getCapacity() >> 3;
	}

	@Override
	protected LongBuffer createBuffer() {
		return BufferUtil.asLongBuffer(getAddress(), getLongCapacity());
	}

}
