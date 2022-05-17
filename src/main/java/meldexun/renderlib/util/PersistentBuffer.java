package meldexun.renderlib.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL44;

public class PersistentBuffer {

	private final int buffer;
	private final ByteBuffer byteBuffer;
	private final FloatBuffer floatBuffer;
	private final IntBuffer intBuffer;

	public PersistentBuffer(long size, int flags) {
		this.buffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		GL44.glBufferStorage(GL15.GL_ARRAY_BUFFER, size, flags | GL44.GL_MAP_PERSISTENT_BIT);
		this.byteBuffer = GL30.glMapBufferRange(GL15.GL_ARRAY_BUFFER, 0, size, flags | GL44.GL_MAP_PERSISTENT_BIT, null);
		this.floatBuffer = byteBuffer.asFloatBuffer();
		this.intBuffer = byteBuffer.asIntBuffer();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public int getBuffer() {
		return buffer;
	}

	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}

	public FloatBuffer getFloatBuffer() {
		return floatBuffer;
	}

	public IntBuffer getIntBuffer() {
		return intBuffer;
	}

	public void dispose() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(buffer);
	}

}
