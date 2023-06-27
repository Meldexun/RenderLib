package meldexun.renderlib.util.memory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public interface BufferFactory<T> {

	BufferFactory<UnsafeBuffer> UNSAFE_BUFFER = of(UnsafeBuffer::new, PrimitiveInfo.BYTE);
	BufferFactory<UnsafeByteBuffer> UNSAFE_BYTE_BUFFER = of(UnsafeByteBuffer::new, PrimitiveInfo.BYTE);
	BufferFactory<UnsafeShortBuffer> UNSAFE_SHORT_BUFFER = of(UnsafeShortBuffer::new, PrimitiveInfo.SHORT);
	BufferFactory<UnsafeIntBuffer> UNSAFE_INT_BUFFER = of(UnsafeIntBuffer::new, PrimitiveInfo.INT);
	BufferFactory<UnsafeLongBuffer> UNSAFE_LONG_BUFFER = of(UnsafeLongBuffer::new, PrimitiveInfo.LONG);
	BufferFactory<UnsafeFloatBuffer> UNSAFE_FLOAT_BUFFER = of(UnsafeFloatBuffer::new, PrimitiveInfo.FLOAT);
	BufferFactory<UnsafeDoubleBuffer> UNSAFE_DOUBLE_BUFFER = of(UnsafeDoubleBuffer::new, PrimitiveInfo.DOUBLE);
	BufferFactory<UnsafeCharBuffer> UNSAFE_CHAR_BUFFER = of(UnsafeCharBuffer::new, PrimitiveInfo.CHAR);

	BufferFactory<ByteBuffer> NIO_BYTE_BUFFER = of(NIOBufferUtil::asByteBuffer, PrimitiveInfo.BYTE);
	BufferFactory<ShortBuffer> NIO_SHORT_BUFFER = of(NIOBufferUtil::asShortBuffer, PrimitiveInfo.SHORT);
	BufferFactory<IntBuffer> NIO_INT_BUFFER = of(NIOBufferUtil::asIntBuffer, PrimitiveInfo.INT);
	BufferFactory<LongBuffer> NIO_LONG_BUFFER = of(NIOBufferUtil::asLongBuffer, PrimitiveInfo.LONG);
	BufferFactory<FloatBuffer> NIO_FLOAT_BUFFER = of(NIOBufferUtil::asFloatBuffer, PrimitiveInfo.FLOAT);
	BufferFactory<DoubleBuffer> NIO_DOUBLE_BUFFER = of(NIOBufferUtil::asDoubleBuffer, PrimitiveInfo.DOUBLE);
	BufferFactory<CharBuffer> NIO_CHAR_BUFFER = of(NIOBufferUtil::asCharBuffer, PrimitiveInfo.CHAR);

	static <T> BufferFactory<T> of(LongLongFunction<T> f, PrimitiveInfo type) {
		return new BufferFactory<T>() {

			@Override
			public T create(long address, long capacity) {
				return f.apply(address, capacity);
			}

			@Override
			public PrimitiveInfo type() {
				return type;
			}

		};
	}

	default T create(long address, long capacity, PrimitiveInfo srcType) {
		return create(address, srcType.convertTo(type(), capacity));
	}

	T create(long address, long capacity);

	PrimitiveInfo type();

}
