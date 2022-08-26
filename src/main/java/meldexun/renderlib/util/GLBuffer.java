package meldexun.renderlib.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL44;

public class GLBuffer {

	private final int buffer;
	private final long size;
	private final boolean persistent;
	private boolean mapped;
	private ByteBuffer byteBuffer;
	private FloatBuffer floatBuffer;
	private IntBuffer intBuffer;

	/**
	 * @param size
	 * @param flags Used when glBufferStorage is supported
	 * @param usage Used when glBufferData is supported
	 */
	public GLBuffer(long size, int flags, int usage) {
		this(size, flags, usage, false, 0);
	}

	/**
	 * @param size
	 * @param flags            Used when glBufferStorage is supported
	 * @param usage            Used when glBufferData is supported
	 * @param persistent
	 * @param persistentAccess
	 */
	public GLBuffer(long size, int flags, int usage, boolean persistent, int persistentAccess) {
		if (persistent && GLUtil.CAPS.OpenGL44) {
			this.buffer = GLUtil.createBuffer(size, flags | GL44.GL_MAP_PERSISTENT_BIT, usage);
			this.size = size;
			this.persistent = true;
			this.mapped = true;
			this.byteBuffer = GLUtil.map(this.buffer, size, persistentAccess | GL44.GL_MAP_PERSISTENT_BIT, 0, null);
		} else {
			this.buffer = GLUtil.createBuffer(size, flags, usage);
			this.size = size;
			this.persistent = false;
		}
	}

	public int getBuffer() {
		return buffer;
	}

	public long getSize() {
		return size;
	}

	/**
	 * @param rangeAccess Used when glMapBufferRange is supported
	 * @param access      Used when glMapBuffer is supported
	 */
	public void map(int rangeAccess, int access) {
		map(size, rangeAccess, access);
	}

	/**
	 * @param length
	 * @param rangeAccess Used when glMapBufferRange is supported
	 * @param access      Used when glMapBuffer is supported
	 */
	public void map(long length, int rangeAccess, int access) {
		if (!persistent && !mapped) {
			byteBuffer = GLUtil.map(buffer, length, rangeAccess, access, byteBuffer);
			mapped = true;
		}
	}

	public boolean isMapped() {
		return mapped;
	}

	public void unmap() {
		if (!persistent) {
			forceUnmap();
		}
	}

	private void forceUnmap() {
		if (mapped) {
			GLUtil.unmap(buffer);
			mapped = false;
			byteBuffer = null;
			floatBuffer = null;
			intBuffer = null;
		}
	}

	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}

	public FloatBuffer getFloatBuffer() {
		if (floatBuffer == null) {
			floatBuffer = byteBuffer.asFloatBuffer();
		}
		return floatBuffer;
	}

	public IntBuffer getIntBuffer() {
		if (intBuffer == null) {
			intBuffer = byteBuffer.asIntBuffer();
		}
		return intBuffer;
	}

	public void dispose() {
		forceUnmap();
		GL15.glDeleteBuffers(buffer);
	}

}
