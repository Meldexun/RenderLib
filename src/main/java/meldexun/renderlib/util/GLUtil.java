package meldexun.renderlib.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import meldexun.matrixutil.Matrix4f;

public class GLUtil {

	private static final FloatBuffer FLOAT_BUFFER = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();

	public static Vector2f getFloat2(int pname) {
		GL11.glGetFloat(pname, FLOAT_BUFFER);
		return new Vector2f(FLOAT_BUFFER.get(0), FLOAT_BUFFER.get(1));
	}

	public static Vector3f getFloat3(int pname) {
		GL11.glGetFloat(pname, FLOAT_BUFFER);
		return new Vector3f(FLOAT_BUFFER.get(0), FLOAT_BUFFER.get(1), FLOAT_BUFFER.get(2));
	}

	public static Vector4f getFloat4(int pname) {
		GL11.glGetFloat(pname, FLOAT_BUFFER);
		return new Vector4f(FLOAT_BUFFER.get(0), FLOAT_BUFFER.get(1), FLOAT_BUFFER.get(2), FLOAT_BUFFER.get(3));
	}

	/**
	 * {@link GL11#GL_PROJECTION_MATRIX},
	 * {@link GL11#GL_MODELVIEW_MATRIX},
	 * {@link GL11#GL_TEXTURE_MATRIX}
	 */
	public static Matrix4f getMatrix(int matrix) {
		GL11.glGetFloat(matrix, FLOAT_BUFFER);
		Matrix4f m = new Matrix4f();
		m.load(FLOAT_BUFFER);
		return m;
	}

	public static void setMatrix(int uniform, Matrix4f matrix) {
		matrix.store(FLOAT_BUFFER);
		GL20.glUniformMatrix4(uniform, false, FLOAT_BUFFER);
	}

}
