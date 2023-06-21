package meldexun.renderlib.util.memory;

import java.nio.ByteBuffer;

public class UnsafeByteBuffer extends UnsafeBufferNIO<ByteBuffer> {

	public UnsafeByteBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.BYTE.toByte(capacity));
	}

	public long getByteCapacity() {
		return PrimitiveInfo.BYTE.fromByte(getCapacity());
	}

	@Override
	protected ByteBuffer createBuffer() {
		return BufferUtil.asByteBuffer(getAddress(), getCapacity());
	}

}
