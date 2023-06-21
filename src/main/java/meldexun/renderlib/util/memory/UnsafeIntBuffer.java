package meldexun.renderlib.util.memory;

import java.nio.IntBuffer;

public class UnsafeIntBuffer extends UnsafeBufferNIO<IntBuffer> {

	public UnsafeIntBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.INT.toByte(capacity));
	}

	public long getIntCapacity() {
		return PrimitiveInfo.INT.fromByte(getCapacity());
	}

	@Override
	protected IntBuffer createBuffer() {
		return BufferUtil.asIntBuffer(getAddress(), getIntCapacity());
	}

}
