package meldexun.renderlib.util.memory;

import java.nio.CharBuffer;

public class UnsafeCharBuffer extends UnsafeBufferNIO<CharBuffer> {

	public UnsafeCharBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.SHORT.toByte(capacity));
	}

	public long getCharCapacity() {
		return PrimitiveInfo.SHORT.fromByte(getCapacity());
	}

	@Override
	protected CharBuffer createBuffer() {
		return BufferUtil.asCharBuffer(getAddress(), getCharCapacity());
	}

}
