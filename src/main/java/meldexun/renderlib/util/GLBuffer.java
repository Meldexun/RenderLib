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

	public GLBuffer(long size, int flags, int usage) {
		this(size, flags, usage, false, 0);
	}

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

	public void map(int accessRange, int access) {
		if (!persistent && !mapped) {
			byteBuffer = GLUtil.map(buffer, size, accessRange, access, byteBuffer);
			mapped = true;
		}
	}

	public void unmap() {
		if (!persistent && mapped) {
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
		if (mapped) {
			GLUtil.unmap(buffer);
		}
		GL15.glDeleteBuffers(buffer);
	}

}
