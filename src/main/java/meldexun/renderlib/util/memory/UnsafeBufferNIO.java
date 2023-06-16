package meldexun.renderlib.util.memory;

import java.nio.Buffer;

public abstract class UnsafeBufferNIO<T extends Buffer> extends UnsafeBuffer {

	private T buffer;

	public UnsafeBufferNIO(long address, long capacity) {
		super(address, capacity);
	}

	public T getBuffer() {
		if (buffer == null) {
			buffer = createBuffer();
		}
		return buffer;
	}

	protected abstract T createBuffer();

}
