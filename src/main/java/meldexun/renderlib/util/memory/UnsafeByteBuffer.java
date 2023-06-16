package meldexun.renderlib.util.memory;

import java.nio.ByteBuffer;

public class UnsafeByteBuffer extends UnsafeBufferNIO<ByteBuffer> {

	public UnsafeByteBuffer(long address, long capacity) {
		super(address, capacity);
	}

	@Override
	protected ByteBuffer createBuffer() {
		return NIOBufferUtil.asByteBuffer(getAddress(), getCapacity());
	}

}
