package meldexun.renderlib.util.memory;

import java.nio.CharBuffer;

public class UnsafeCharBuffer extends UnsafeNIOBuffer<CharBuffer> {

	public UnsafeCharBuffer(long address, long capacity) {
		super(address, PrimitiveInfo.SHORT.toByte(capacity));
	}

	public long getCharCapacity() {
		return PrimitiveInfo.SHORT.fromByte(getCapacity());
	}

	@Override
	protected CharBuffer createBuffer() {
		return NIOBufferUtil.asCharBuffer(getAddress(), getCharCapacity());
	}

}
