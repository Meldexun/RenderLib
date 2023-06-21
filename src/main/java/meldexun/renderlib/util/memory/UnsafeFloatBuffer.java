package meldexun.renderlib.util.memory;

import java.nio.FloatBuffer;

public class UnsafeFloatBuffer extends UnsafeBufferNIO<FloatBuffer> {

	public UnsafeFloatBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.FLOAT.toByte(capacity));
	}

	public long getFloatCapacity() {
		return PrimitiveInfo.FLOAT.fromByte(getCapacity());
	}

	@Override
	protected FloatBuffer createBuffer() {
		return BufferUtil.asFloatBuffer(getAddress(), getFloatCapacity());
	}

}
