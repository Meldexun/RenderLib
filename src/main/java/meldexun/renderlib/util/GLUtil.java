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
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GL44;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.KHRDebugCallback;

import meldexun.matrixutil.Matrix4f;
import meldexun.renderlib.RenderLib;
import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.util.memory.BufferUtil;
import meldexun.renderlib.util.memory.MemoryAccess;
import meldexun.renderlib.util.memory.UnsafeFloatBuffer;

public class GLUtil {

	public static ContextCapabilities CAPS;
	private static final UnsafeFloatBuffer FLOAT_BUFFER = BufferUtil.allocateFloat(16);

	public static void init() {
		CAPS = GLContext.getCapabilities();

		RenderLib.LOGGER.info("OpenGL Vendor: {}", GL11.glGetString(GL11.GL_VENDOR));
		RenderLib.LOGGER.info("OpenGL Renderer: {}", GL11.glGetString(GL11.GL_RENDERER));
		RenderLib.LOGGER.info("OpenGL Version: {}", GL11.glGetString(GL11.GL_VERSION));

		if (!RenderLibConfig.logOpenGLExtensions)
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

	public static void updateDebugOutput() {
		if (CAPS == null)
			return;
		RenderLib.LOGGER.info("OpenGL Debug: supported={}, enabled={}", CAPS.OpenGL43, RenderLibConfig.openGLDebugOutput.enabled);

		if (!CAPS.OpenGL43)
			return;
		if (RenderLibConfig.openGLDebugOutput.enabled) {
			GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
			GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);

			GL43.glDebugMessageCallback(new KHRDebugCallback((source, type, id, severity, message) -> {
				if (type == GL43.GL_DEBUG_TYPE_ERROR) {
					throw new GLException(String.format("OpenGL Error: %s %s %s %s %s", getSource(source), getType(type), getSeverity(severity), id, message));
				} else {
					RenderLib.LOGGER.info("OpenGL Debug: {} {} {} {} {}", getSource(source), getType(type), getSeverity(severity), id, message);
				}
			}));

			GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, null, false);
			GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL43.GL_DEBUG_TYPE_ERROR, GL11.GL_DONT_CARE, null, true);
			if (RenderLibConfig.openGLDebugOutput.logHighSeverity)
				GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL43.GL_DEBUG_SEVERITY_HIGH, null, true);
			if (RenderLibConfig.openGLDebugOutput.logMediumSeverity)
				GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL43.GL_DEBUG_SEVERITY_MEDIUM, null, true);
			if (RenderLibConfig.openGLDebugOutput.logLowSeverity)
				GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL43.GL_DEBUG_SEVERITY_LOW, null, true);
			if (RenderLibConfig.openGLDebugOutput.logNotificationSeverity)
				GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL43.GL_DEBUG_SEVERITY_NOTIFICATION, null, true);
		} else {
			GL11.glDisable(GL43.GL_DEBUG_OUTPUT);
			GL11.glDisable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
		}
	}

	private static String getSource(int source) {
		switch (source) {
		case GL43.GL_DEBUG_SOURCE_API:
			return "API";
		case GL43.GL_DEBUG_SOURCE_WINDOW_SYSTEM:
			return "WINDOW SYSTEM";
		case GL43.GL_DEBUG_SOURCE_SHADER_COMPILER:
			return "SHADER COMPILER";
		case GL43.GL_DEBUG_SOURCE_THIRD_PARTY:
			return "THIRD PARTY";
		case GL43.GL_DEBUG_SOURCE_APPLICATION:
			return "APPLICATION";
		case GL43.GL_DEBUG_SOURCE_OTHER:
			return "OTHER";
		default:
			return "?";
		}
	}

	private static String getType(int type) {
		switch (type) {
		case GL43.GL_DEBUG_TYPE_ERROR:
			return "ERROR";
		case GL43.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
			return "DEPRECATED BEHAVIOR";
		case GL43.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
			return "UNDEFINED BEHAVIOR";
		case GL43.GL_DEBUG_TYPE_PORTABILITY:
			return "PORTABILITY";
		case GL43.GL_DEBUG_TYPE_PERFORMANCE:
			return "PERFORMANCE";
		case GL43.GL_DEBUG_TYPE_OTHER:
			return "OTHER";
		case GL43.GL_DEBUG_TYPE_MARKER:
			return "MARKER";
		default:
			return "?";
		}
	}

	private static String getSeverity(int severity) {
		switch (severity) {
		case GL43.GL_DEBUG_SEVERITY_HIGH:
			return "HIGH";
		case GL43.GL_DEBUG_SEVERITY_MEDIUM:
			return "MEDIUM";
		case GL43.GL_DEBUG_SEVERITY_LOW:
			return "LOW";
		case GL43.GL_DEBUG_SEVERITY_NOTIFICATION:
			return "NOTIFICATION";
		default:
			return "?";
		}
	}

	public static MemoryAccess getFloat(int pname) {
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

}
