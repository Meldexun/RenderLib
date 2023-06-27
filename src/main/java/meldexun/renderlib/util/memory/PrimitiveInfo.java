package meldexun.renderlib.util.memory;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_DOUBLE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_FLOAT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_LONG_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_SHORT_BASE_OFFSET;

public enum PrimitiveInfo {

	BYTE(ARRAY_BYTE_BASE_OFFSET, 0),
	SHORT(ARRAY_SHORT_BASE_OFFSET, 1),
	INT(ARRAY_INT_BASE_OFFSET, 2),
	LONG(ARRAY_LONG_BASE_OFFSET, 3),
	FLOAT(ARRAY_FLOAT_BASE_OFFSET, 2),
	DOUBLE(ARRAY_DOUBLE_BASE_OFFSET, 3),
	CHAR(ARRAY_CHAR_BASE_OFFSET, 1);

	private final long arrayBaseOffset;
	private final long offset;

	private PrimitiveInfo(long arrayBaseOffset, long offset) {
		this.arrayBaseOffset = arrayBaseOffset;
		this.offset = offset;
	}

	public long arrayBaseOffset() {
		return arrayBaseOffset;
	}

	public long toByte(long x) {
		return x << offset;
	}

	public long fromByte(long x) {
		return x >> offset;
	}

	public long convertTo(PrimitiveInfo type, long x) {
		return (x << offset) >> type.offset;
	}

}
