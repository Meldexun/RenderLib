package meldexun.renderlib.util.memory;

import java.nio.ByteBuffer;

public class UnsafeByteBuffer extends UnsafeNIOBuffer<ByteBuffer> {

	public UnsafeByteBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.BYTE.toByte(capacity));
	}

	public long getByteCapacity() {
		return PrimitiveInfo.BYTE.fromByte(getCapacity());
	}

	@Override
	protected ByteBuffer createBuffer() {
		return NIOBufferUtil.asByteBuffer(getAddress(), getCapacity());
	}

}
