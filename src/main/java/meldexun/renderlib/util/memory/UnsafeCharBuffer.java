package meldexun.renderlib.util.memory;

import java.nio.CharBuffer;

public class UnsafeCharBuffer extends UnsafeBufferNIO<CharBuffer> {

	public UnsafeCharBuffer(long address, long capacity) {
		super(address, capacity << 1);
	}

	public long getCharCapacity() {
		return getCapacity() >> 1;
	}

	@Override
	protected CharBuffer createBuffer() {
		return NIOBufferUtil.asCharBuffer(getAddress(), getCharCapacity());
	}

}
