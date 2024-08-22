package meldexun.renderlib.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL44;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GLContext;

import meldexun.matrixutil.Matrix4f;
import meldexun.memoryutil.UnsafeBufferUtil;
import meldexun.memoryutil.UnsafeFloatBuffer;
import meldexun.renderlib.RenderLib;
import meldexun.renderlib.config.RenderLibConfig;
import net.minecraft.client.renderer.GlStateManager;

public class GLUtil {

	public static ContextCapabilities CAPS;
	private static final UnsafeFloatBuffer FLOAT_BUFFER = UnsafeBufferUtil.allocateFloat(16);
	private static boolean blend;
	private static int blendSrcFactor;
	private static int blendDstFactor;
	private static int blendSrcFactorAlpha;
	private static int blendDstFactorAlpha;
	private static boolean depthTest;
	private static int depthFunc;
	private static boolean depthMask;
	private static boolean cull;
	private static int cullFace;
	private static boolean colorMaskRed;
	private static boolean colorMaskGreen;
	private static boolean colorMaskBlue;
	private static boolean colorMaskAlpha;

	public static void init() {
		CAPS = GLContext.getCapabilities();

		RenderLib.LOGGER.info("OpenGL Vendor: {}", GL11.glGetString(GL11.GL_VENDOR));
		RenderLib.LOGGER.info("OpenGL Renderer: {}", GL11.glGetString(GL11.GL_RENDERER));
		RenderLib.LOGGER.info("OpenGL Version: {}", GL11.glGetString(GL11.GL_VERSION));

		if (!RenderLibConfig.openGLLogExtensions)
			return;

		RenderLib.LOGGER.info("OpenGL Extensions:");
		int longestExtensionName = streamExtensionFields().map(Field::getName)
				.mapToInt(String::length)
				.max()
				.orElse(0);
		streamExtensionFields().forEach(field -> {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append(field.getName());
				while (sb.length() < longestExtensionName + 1)
					sb.append(' ');
				sb.append(field.getBoolean(CAPS));
				RenderLib.LOGGER.info(sb);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				RenderLib.LOGGER.error("Failed logging OpenGL extension with name '{}'", field.getName(), e);
			}
		});
	}

	private static Stream<Field> streamExtensionFields() {
		return Arrays.stream(ContextCapabilities.class.getFields())
				.filter(field -> !Modifier.isStatic(field.getModifiers()))
				.filter(field -> field.getType() == boolean.class);
	}

	public static UnsafeFloatBuffer getFloat(int pname) {
		GL11.glGetFloat(pname, FLOAT_BUFFER.getBuffer());
		return FLOAT_BUFFER;
	}

	/**
	 * {@link GL11#GL_PROJECTION_MATRIX},
	 * {@link GL11#GL_MODELVIEW_MATRIX},
	 * {@link GL11#GL_TEXTURE_MATRIX}
	 */
	public static Matrix4f getMatrix(int matrix) {
		GL11.glGetFloat(matrix, FLOAT_BUFFER.getBuffer());
		Matrix4f m = new Matrix4f();
		m.load(FLOAT_BUFFER.getAddress());
		return m;
	}

	public static void setMatrix(int uniform, Matrix4f matrix) {
		matrix.store(FLOAT_BUFFER.getAddress());
		GL20.glUniformMatrix4(uniform, false, FLOAT_BUFFER.getBuffer());
	}

	public static int createBuffer(long size, int flags, int usage) {
		if (CAPS.OpenGL45) {
			int buffer = GL45.glCreateBuffers();
			GL45.glNamedBufferStorage(buffer, size, flags);
			return buffer;
		} else if (CAPS.OpenGL44) {
			int buffer = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
			GL44.glBufferStorage(GL15.GL_ARRAY_BUFFER, size, flags);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			return buffer;
		} else {
			int buffer = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size, usage);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			return buffer;
		}
	}

	public static ByteBuffer map(int buffer, long length, int accessRange, int access, @Nullable ByteBuffer oldBuffer) {
		if (CAPS.OpenGL45) {
			return GL45.glMapNamedBufferRange(buffer, 0L, length, accessRange, oldBuffer);
		} else if (CAPS.OpenGL30) {
			if (!CAPS.OpenGL44) {
				accessRange &= ~(GL44.GL_MAP_PERSISTENT_BIT | GL44.GL_MAP_COHERENT_BIT);
			}
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
			ByteBuffer byteBuffer = GL30.glMapBufferRange(GL15.GL_ARRAY_BUFFER, 0L, length, accessRange, oldBuffer);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			return byteBuffer;
		} else {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
			ByteBuffer byteBuffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, access, length, oldBuffer);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			return byteBuffer;
		}
	}

	public static void unmap(int buffer) {
		if (CAPS.OpenGL45) {
			GL45.glUnmapNamedBuffer(buffer);
		} else {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
			GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
	}

	public static void saveShaderGLState() {
		blend = GlStateManager.blendState.blend.currentState;
		blendSrcFactor = GlStateManager.blendState.srcFactor;
		blendDstFactor = GlStateManager.blendState.dstFactor;
		blendSrcFactorAlpha = GlStateManager.blendState.srcFactorAlpha;
		blendDstFactorAlpha = GlStateManager.blendState.dstFactorAlpha;

		depthTest = GlStateManager.depthState.depthTest.currentState;
		depthFunc = GlStateManager.depthState.depthFunc;
		depthMask = GlStateManager.depthState.maskEnabled;

		cull = GlStateManager.cullState.cullFace.currentState;
		cullFace = GlStateManager.cullState.mode;

		colorMaskRed = GlStateManager.colorMaskState.red;
		colorMaskGreen = GlStateManager.colorMaskState.green;
		colorMaskBlue = GlStateManager.colorMaskState.blue;
		colorMaskAlpha = GlStateManager.colorMaskState.alpha;
	}

	public static void restoreShaderGLState() {
		if (blend) {
			GlStateManager.enableBlend();
		} else {
			GlStateManager.disableBlend();
		}
		GlStateManager.tryBlendFuncSeparate(blendSrcFactor, blendDstFactor, blendSrcFactorAlpha, blendDstFactorAlpha);

		if (depthTest) {
			GlStateManager.enableDepth();
		} else {
			GlStateManager.disableDepth();
		}
		GlStateManager.depthFunc(depthFunc);
		GlStateManager.depthMask(depthMask);

		if (cull) {
			GlStateManager.enableCull();
		} else {
			GlStateManager.disableCull();
		}
		GlStateManager.cullFace(cullFace);

		GlStateManager.colorMask(colorMaskRed, colorMaskGreen, colorMaskBlue, colorMaskAlpha);
	}

}
