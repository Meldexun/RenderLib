package meldexun.renderlib.util.memory;

public class UnsafeBuffer implements MemoryAccess, AutoCloseable {

	private final long address;
	private final long capacity;

	public UnsafeBuffer(long address, long capacity) {
		this.address = address;
		this.capacity = capacity;
	}

	@Override
	public long getAddress() {
		return address;
	}

	public long getCapacity() {
		return capacity;
	}

	@Override
	public void close() {
		BufferUtil.freeMemory(address);
	}

}
