package meldexun.renderlib.util.memory;

import java.nio.IntBuffer;

public class UnsafeIntBuffer extends UnsafeBufferNIO<IntBuffer> {

	public UnsafeIntBuffer(long address, long capacity) {
		super(address, capacity << 2);
	}

	public long getIntCapacity() {
		return getCapacity() >> 2;
	}

	@Override
	protected IntBuffer createBuffer() {
		return NIOBufferUtil.asIntBuffer(getAddress(), getIntCapacity());
	}

}
