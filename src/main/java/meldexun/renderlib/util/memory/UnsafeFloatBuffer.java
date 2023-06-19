package meldexun.renderlib.util.memory;

import java.nio.FloatBuffer;

public class UnsafeFloatBuffer extends UnsafeBufferNIO<FloatBuffer> {

	public UnsafeFloatBuffer(long address, long capacity) {
		super(address, capacity << 2);
	}

	public long getFloatCapacity() {
		return getCapacity() >> 2;
	}

	@Override
	protected FloatBuffer createBuffer() {
		return BufferUtil.asFloatBuffer(getAddress(), getFloatCapacity());
	}

}
