package meldexun.renderlib.util.memory;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_DOUBLE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_FLOAT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_LONG_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_SHORT_BASE_OFFSET;

enum PrimitiveInfo {

	BYTE {
		@Override
		public long arrayBaseOffset() {
			return ARRAY_BYTE_BASE_OFFSET;
		}

		@Override
		public long toByte(long x) {
			return x;
		}

		@Override
		public long fromByte(long x) {
			return x;
		}
	},
	SHORT {
		@Override
		public long arrayBaseOffset() {
			return ARRAY_SHORT_BASE_OFFSET;
		}

		@Override
		public long toByte(long x) {
			return x << 1;
		}

		@Override
		public long fromByte(long x) {
			return x >> 1;
		}
	},
	INT {
		@Override
		public long arrayBaseOffset() {
			return ARRAY_INT_BASE_OFFSET;
		}

		@Override
		public long toByte(long x) {
			return x << 2;
		}

		@Override
		public long fromByte(long x) {
			return x >> 2;
		}
	},
	LONG {
		@Override
		public long arrayBaseOffset() {
			return ARRAY_LONG_BASE_OFFSET;
		}

		@Override
		public long toByte(long x) {
			return x << 3;
		}

		@Override
		public long fromByte(long x) {
			return x >> 3;
		}
	},
	FLOAT {
		@Override
		public long arrayBaseOffset() {
			return ARRAY_FLOAT_BASE_OFFSET;
		}

		@Override
		public long toByte(long x) {
			return x << 2;
		}

		@Override
		public long fromByte(long x) {
			return x >> 2;
		}
	},
	DOUBLE {
		@Override
		public long arrayBaseOffset() {
			return ARRAY_DOUBLE_BASE_OFFSET;
		}

		@Override
		public long toByte(long x) {
			return x << 3;
		}

		@Override
		public long fromByte(long x) {
			return x >> 3;
		}
	},
	CHAR {
		@Override
		public long arrayBaseOffset() {
			return ARRAY_CHAR_BASE_OFFSET;
		}

		@Override
		public long toByte(long x) {
			return x << 1;
		}

		@Override
		public long fromByte(long x) {
			return x >> 1;
		}
	};

	public abstract long arrayBaseOffset();

	public abstract long toByte(long x);

	public abstract long fromByte(long x);

}
