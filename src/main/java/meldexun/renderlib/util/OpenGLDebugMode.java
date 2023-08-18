package meldexun.renderlib.util;

import javax.annotation.Nullable;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.KHRDebug;
import org.lwjgl.opengl.KHRDebugCallback;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.config.RenderLibConfig.OpenGLDebugOutput;

public enum OpenGLDebugMode {

	OpenGL43 {
		@Override
		public void enable(OpenGLDebugOutput openGLDebugOutput) {
			GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
			GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);

			GL43.glDebugMessageCallback(new KHRDebugCallback(this::log));

			GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, null, false);
			GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL43.GL_DEBUG_TYPE_ERROR, GL11.GL_DONT_CARE, null, true);
			if (openGLDebugOutput.logHighSeverity) {
				GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL43.GL_DEBUG_SEVERITY_HIGH, null, true);
			}
			if (openGLDebugOutput.logMediumSeverity) {
				GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL43.GL_DEBUG_SEVERITY_MEDIUM, null, true);
			}
			if (openGLDebugOutput.logLowSeverity) {
				GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL43.GL_DEBUG_SEVERITY_LOW, null, true);
			}
			if (openGLDebugOutput.logNotificationSeverity) {
				GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL43.GL_DEBUG_SEVERITY_NOTIFICATION, null, true);
			}
		}

		@Override
		public void disable() {
			GL11.glDisable(GL43.GL_DEBUG_OUTPUT);
			GL11.glDisable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);

			GL43.glDebugMessageCallback(null);

			GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, null, false);
		}

		@Override
		protected void log(int source, int type, int id, int severity, String message) {
			if (type == GL43.GL_DEBUG_TYPE_ERROR) {
				this.throwGLException(source, type, id, severity, message);
			} else {
				this.logDebugMessage(source, type, id, severity, message);
			}
		}

		@Override
		protected String getSource(int source) {
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

		@Override
		protected String getType(int type) {
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

		@Override
		protected String getSeverity(int severity) {
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

	},
	KHR {
		@Override
		public void enable(OpenGLDebugOutput openGLDebugOutput) {
			GL11.glEnable(KHRDebug.GL_DEBUG_OUTPUT);
			GL11.glEnable(KHRDebug.GL_DEBUG_OUTPUT_SYNCHRONOUS);

			KHRDebug.glDebugMessageCallback(new KHRDebugCallback(this::log));

			KHRDebug.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, null, false);
			KHRDebug.glDebugMessageControl(GL11.GL_DONT_CARE, KHRDebug.GL_DEBUG_TYPE_ERROR, GL11.GL_DONT_CARE, null, true);
			if (openGLDebugOutput.logHighSeverity) {
				KHRDebug.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, KHRDebug.GL_DEBUG_SEVERITY_HIGH, null, true);
			}
			if (openGLDebugOutput.logMediumSeverity) {
				KHRDebug.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, KHRDebug.GL_DEBUG_SEVERITY_MEDIUM, null, true);
			}
			if (openGLDebugOutput.logLowSeverity) {
				KHRDebug.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, KHRDebug.GL_DEBUG_SEVERITY_LOW, null, true);
			}
			if (openGLDebugOutput.logNotificationSeverity) {
				KHRDebug.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION, null, true);
			}
		}

		@Override
		public void disable() {
			GL11.glDisable(KHRDebug.GL_DEBUG_OUTPUT);
			GL11.glDisable(KHRDebug.GL_DEBUG_OUTPUT_SYNCHRONOUS);

			KHRDebug.glDebugMessageCallback(null);

			KHRDebug.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, null, false);
		}

		@Override
		protected void log(int source, int type, int id, int severity, String message) {
			if (type == KHRDebug.GL_DEBUG_TYPE_ERROR) {
				this.throwGLException(source, type, id, severity, message);
			} else {
				this.logDebugMessage(source, type, id, severity, message);
			}
		}

		@Override
		protected String getSource(int source) {
			switch (source) {
			case KHRDebug.GL_DEBUG_SOURCE_API:
				return "API";
			case KHRDebug.GL_DEBUG_SOURCE_WINDOW_SYSTEM:
				return "WINDOW SYSTEM";
			case KHRDebug.GL_DEBUG_SOURCE_SHADER_COMPILER:
				return "SHADER COMPILER";
			case KHRDebug.GL_DEBUG_SOURCE_THIRD_PARTY:
				return "THIRD PARTY";
			case KHRDebug.GL_DEBUG_SOURCE_APPLICATION:
				return "APPLICATION";
			case KHRDebug.GL_DEBUG_SOURCE_OTHER:
				return "OTHER";
			default:
				return "?";
			}
		}

		@Override
		protected String getType(int type) {
			switch (type) {
			case KHRDebug.GL_DEBUG_TYPE_ERROR:
				return "ERROR";
			case KHRDebug.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
				return "DEPRECATED BEHAVIOR";
			case KHRDebug.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
				return "UNDEFINED BEHAVIOR";
			case KHRDebug.GL_DEBUG_TYPE_PORTABILITY:
				return "PORTABILITY";
			case KHRDebug.GL_DEBUG_TYPE_PERFORMANCE:
				return "PERFORMANCE";
			case KHRDebug.GL_DEBUG_TYPE_OTHER:
				return "OTHER";
			case KHRDebug.GL_DEBUG_TYPE_MARKER:
				return "MARKER";
			default:
				return "?";
			}
		}

		@Override
		protected String getSeverity(int severity) {
			switch (severity) {
			case KHRDebug.GL_DEBUG_SEVERITY_HIGH:
				return "HIGH";
			case KHRDebug.GL_DEBUG_SEVERITY_MEDIUM:
				return "MEDIUM";
			case KHRDebug.GL_DEBUG_SEVERITY_LOW:
				return "LOW";
			case KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION:
				return "NOTIFICATION";
			default:
				return "?";
			}
		}

	};

	@Nullable
	public static OpenGLDebugMode getSupported() {
		ContextCapabilities capabilities = GLContext.getCapabilities();
		if (capabilities.OpenGL43) {
			return OpenGL43;
		}
		if (capabilities.GL_KHR_debug) {
			return KHR;
		}
		return null;
	}

	public abstract void enable(OpenGLDebugOutput openGLDebugOutput);

	public abstract void disable();

	protected abstract void log(int source, int type, int id, int severity, String message);

	protected void throwGLException(int source, int type, int id, int severity, String message) {
		throw new GLException(String.format("OpenGL Error: %s %s %s %s %s", getSource(source), getType(type), getSeverity(severity), id, message));
	}

	protected void logDebugMessage(int source, int type, int id, int severity, String message) {
		RenderLib.LOGGER.info("OpenGL Debug: {} {} {} {} {}", getSource(source), getType(type), getSeverity(severity), id, message);
	}

	protected abstract String getSource(int source);

	protected abstract String getType(int source);

	protected abstract String getSeverity(int source);

}
