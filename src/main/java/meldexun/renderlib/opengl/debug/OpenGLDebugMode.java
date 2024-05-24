package meldexun.renderlib.opengl.debug;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.KHRDebug;
import org.lwjgl.opengl.KHRDebugCallback;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.config.RenderLibConfig.OpenGLDebugConfiguration;
import meldexun.renderlib.util.GLException;

public enum OpenGLDebugMode {

	OpenGL43 {

		@Override
		public void enable(GLDebugMessageFilter[] messageFilters) {
			GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
			GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);

			GL43.glDebugMessageCallback(new KHRDebugCallback(this::log));

			GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, null, false);
			for (GLDebugMessageFilter messageFilter : messageFilters) {
				int source = this.getSource(messageFilter.source);
				int type = this.getType(messageFilter.type);
				int severity = this.getSeverity(messageFilter.severity);
				GL43.glDebugMessageControl(source, type, severity, null, messageFilter.enabled);
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
		protected Source getSource(int source) {
			switch (source) {
			case GL43.GL_DEBUG_SOURCE_API:
				return Source.API;
			case GL43.GL_DEBUG_SOURCE_WINDOW_SYSTEM:
				return Source.WINDOW_SYSTEM;
			case GL43.GL_DEBUG_SOURCE_SHADER_COMPILER:
				return Source.SHADER_COMPILER;
			case GL43.GL_DEBUG_SOURCE_THIRD_PARTY:
				return Source.THIRD_PARTY;
			case GL43.GL_DEBUG_SOURCE_APPLICATION:
				return Source.APPLICATION;
			case GL43.GL_DEBUG_SOURCE_OTHER:
				return Source.OTHER;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected int getSource(Source source) {
			switch (source) {
			case ANY:
				return GL11.GL_DONT_CARE;
			case API:
				return GL43.GL_DEBUG_SOURCE_API;
			case WINDOW_SYSTEM:
				return GL43.GL_DEBUG_SOURCE_WINDOW_SYSTEM;
			case SHADER_COMPILER:
				return GL43.GL_DEBUG_SOURCE_SHADER_COMPILER;
			case THIRD_PARTY:
				return GL43.GL_DEBUG_SOURCE_THIRD_PARTY;
			case APPLICATION:
				return GL43.GL_DEBUG_SOURCE_APPLICATION;
			case OTHER:
				return GL43.GL_DEBUG_SOURCE_OTHER;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected Type getType(int type) {
			switch (type) {
			case GL43.GL_DEBUG_TYPE_ERROR:
				return Type.ERROR;
			case GL43.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
				return Type.DEPRECATED_BEHAVIOR;
			case GL43.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
				return Type.UNDEFINED_BEHAVIOR;
			case GL43.GL_DEBUG_TYPE_PORTABILITY:
				return Type.PORTABILITY;
			case GL43.GL_DEBUG_TYPE_PERFORMANCE:
				return Type.PERFORMANCE;
			case GL43.GL_DEBUG_TYPE_MARKER:
				return Type.MARKER;
			case GL43.GL_DEBUG_TYPE_PUSH_GROUP:
				return Type.PUSH_GROUP;
			case GL43.GL_DEBUG_TYPE_POP_GROUP:
				return Type.POP_GROUP;
			case GL43.GL_DEBUG_TYPE_OTHER:
				return Type.OTHER;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected int getType(Type type) {
			switch (type) {
			case ANY:
				return GL11.GL_DONT_CARE;
			case ERROR:
				return GL43.GL_DEBUG_TYPE_ERROR;
			case DEPRECATED_BEHAVIOR:
				return GL43.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR;
			case UNDEFINED_BEHAVIOR:
				return GL43.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR;
			case PORTABILITY:
				return GL43.GL_DEBUG_TYPE_PORTABILITY;
			case PERFORMANCE:
				return GL43.GL_DEBUG_TYPE_PERFORMANCE;
			case MARKER:
				return GL43.GL_DEBUG_TYPE_MARKER;
			case PUSH_GROUP:
				return GL43.GL_DEBUG_TYPE_PUSH_GROUP;
			case POP_GROUP:
				return GL43.GL_DEBUG_TYPE_POP_GROUP;
			case OTHER:
				return GL43.GL_DEBUG_TYPE_OTHER;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected Severity getSeverity(int severity) {
			switch (severity) {
			case GL43.GL_DEBUG_SEVERITY_HIGH:
				return Severity.HIGH;
			case GL43.GL_DEBUG_SEVERITY_MEDIUM:
				return Severity.MEDIUM;
			case GL43.GL_DEBUG_SEVERITY_LOW:
				return Severity.LOW;
			case GL43.GL_DEBUG_SEVERITY_NOTIFICATION:
				return Severity.NOTIFICATION;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected int getSeverity(Severity severity) {
			switch (severity) {
			case ANY:
				return GL11.GL_DONT_CARE;
			case HIGH:
				return GL43.GL_DEBUG_SEVERITY_HIGH;
			case MEDIUM:
				return GL43.GL_DEBUG_SEVERITY_MEDIUM;
			case LOW:
				return GL43.GL_DEBUG_SEVERITY_LOW;
			case NOTIFICATION:
				return GL43.GL_DEBUG_SEVERITY_NOTIFICATION;
			default:
				throw new IllegalArgumentException();
			}
		}

	},
	KHR {

		@Override
		public void enable(GLDebugMessageFilter[] messageFilters) {
			GL11.glEnable(KHRDebug.GL_DEBUG_OUTPUT);
			GL11.glEnable(KHRDebug.GL_DEBUG_OUTPUT_SYNCHRONOUS);

			KHRDebug.glDebugMessageCallback(new KHRDebugCallback(this::log));

			KHRDebug.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, null, false);
			for (GLDebugMessageFilter messageFilter : messageFilters) {
				int source = this.getSource(messageFilter.source);
				int type = this.getType(messageFilter.type);
				int severity = this.getSeverity(messageFilter.severity);
				KHRDebug.glDebugMessageControl(source, type, severity, null, messageFilter.enabled);
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
		protected Source getSource(int source) {
			switch (source) {
			case KHRDebug.GL_DEBUG_SOURCE_API:
				return Source.API;
			case KHRDebug.GL_DEBUG_SOURCE_WINDOW_SYSTEM:
				return Source.WINDOW_SYSTEM;
			case KHRDebug.GL_DEBUG_SOURCE_SHADER_COMPILER:
				return Source.SHADER_COMPILER;
			case KHRDebug.GL_DEBUG_SOURCE_THIRD_PARTY:
				return Source.THIRD_PARTY;
			case KHRDebug.GL_DEBUG_SOURCE_APPLICATION:
				return Source.APPLICATION;
			case KHRDebug.GL_DEBUG_SOURCE_OTHER:
				return Source.OTHER;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected int getSource(Source source) {
			switch (source) {
			case ANY:
				return GL11.GL_DONT_CARE;
			case API:
				return KHRDebug.GL_DEBUG_SOURCE_API;
			case WINDOW_SYSTEM:
				return KHRDebug.GL_DEBUG_SOURCE_WINDOW_SYSTEM;
			case SHADER_COMPILER:
				return KHRDebug.GL_DEBUG_SOURCE_SHADER_COMPILER;
			case THIRD_PARTY:
				return KHRDebug.GL_DEBUG_SOURCE_THIRD_PARTY;
			case APPLICATION:
				return KHRDebug.GL_DEBUG_SOURCE_APPLICATION;
			case OTHER:
				return KHRDebug.GL_DEBUG_SOURCE_OTHER;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected Type getType(int type) {
			switch (type) {
			case KHRDebug.GL_DEBUG_TYPE_ERROR:
				return Type.ERROR;
			case KHRDebug.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
				return Type.DEPRECATED_BEHAVIOR;
			case KHRDebug.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
				return Type.UNDEFINED_BEHAVIOR;
			case KHRDebug.GL_DEBUG_TYPE_PORTABILITY:
				return Type.PORTABILITY;
			case KHRDebug.GL_DEBUG_TYPE_PERFORMANCE:
				return Type.PERFORMANCE;
			case KHRDebug.GL_DEBUG_TYPE_MARKER:
				return Type.MARKER;
			case KHRDebug.GL_DEBUG_TYPE_PUSH_GROUP:
				return Type.PUSH_GROUP;
			case KHRDebug.GL_DEBUG_TYPE_POP_GROUP:
				return Type.POP_GROUP;
			case KHRDebug.GL_DEBUG_TYPE_OTHER:
				return Type.OTHER;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected int getType(Type type) {
			switch (type) {
			case ANY:
				return GL11.GL_DONT_CARE;
			case ERROR:
				return KHRDebug.GL_DEBUG_TYPE_ERROR;
			case DEPRECATED_BEHAVIOR:
				return KHRDebug.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR;
			case UNDEFINED_BEHAVIOR:
				return KHRDebug.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR;
			case PORTABILITY:
				return KHRDebug.GL_DEBUG_TYPE_PORTABILITY;
			case PERFORMANCE:
				return KHRDebug.GL_DEBUG_TYPE_PERFORMANCE;
			case MARKER:
				return KHRDebug.GL_DEBUG_TYPE_MARKER;
			case PUSH_GROUP:
				return KHRDebug.GL_DEBUG_TYPE_PUSH_GROUP;
			case POP_GROUP:
				return KHRDebug.GL_DEBUG_TYPE_POP_GROUP;
			case OTHER:
				return KHRDebug.GL_DEBUG_TYPE_OTHER;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected Severity getSeverity(int severity) {
			switch (severity) {
			case KHRDebug.GL_DEBUG_SEVERITY_HIGH:
				return Severity.HIGH;
			case KHRDebug.GL_DEBUG_SEVERITY_MEDIUM:
				return Severity.MEDIUM;
			case KHRDebug.GL_DEBUG_SEVERITY_LOW:
				return Severity.LOW;
			case KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION:
				return Severity.NOTIFICATION;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		protected int getSeverity(Severity severity) {
			switch (severity) {
			case ANY:
				return GL11.GL_DONT_CARE;
			case HIGH:
				return KHRDebug.GL_DEBUG_SEVERITY_HIGH;
			case MEDIUM:
				return KHRDebug.GL_DEBUG_SEVERITY_MEDIUM;
			case LOW:
				return KHRDebug.GL_DEBUG_SEVERITY_LOW;
			case NOTIFICATION:
				return KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION;
			default:
				throw new IllegalArgumentException();
			}
		}

	};

	private static OpenGLDebugConfiguration currentConfig;

	public static void setupDebugOutput(OpenGLDebugConfiguration config) {
		OpenGLDebugMode debugMode = OpenGLDebugMode.getSupported();
		RenderLib.LOGGER.info("OpenGL Debug: supported={}, enabled={}", debugMode, config.enabled);

		if (debugMode == null) {
			return;
		}

		currentConfig = config;
		if (config.enabled) {
			debugMode.enable(config.getMessageFilters());
		} else {
			debugMode.disable();
		}
	}

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

	public abstract void enable(GLDebugMessageFilter[] messageFilters);

	public abstract void disable();

	protected void log(int source, int type, int id, int severity, String message) {
		OpenGLDebugMode.log(this.getSource(source), this.getType(type), this.getSeverity(severity), id, message);
	}

	private static void log(Source source, Type type, Severity severity, int id, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("OpenGL");
		sb.append(' ');
		sb.append(source);
		sb.append(' ');
		sb.append(type);
		sb.append(' ');
		sb.append(severity);
		sb.append(' ');
		sb.append(id);
		sb.append(' ');
		sb.append(message);

		if (type == Type.ERROR && currentConfig.crashOnError) {
			throw new GLException(sb.toString());
		}

		if (currentConfig.logStackTrace.shouldLogStackTrace(source, type, severity, id)) {
			sb.append('\n');
			for (StackTraceElement stackTraceElement : new Exception().getStackTrace()) {
				sb.append('\t');
				sb.append("at");
				sb.append(' ');
				sb.append(stackTraceElement.getClassName());
				sb.append('.');
				sb.append(stackTraceElement.getMethodName());
				sb.append('(');
				sb.append(stackTraceElement.getFileName());
				sb.append(':');
				sb.append(stackTraceElement.getLineNumber());
				sb.append(')');
				sb.append('\n');
			}
			sb.deleteCharAt(sb.length() - 1);
		}

		RenderLib.LOGGER.log(type == Type.ERROR ? Level.ERROR : Level.INFO, sb);
	}

	protected abstract Source getSource(int source);

	protected abstract int getSource(Source source);

	protected abstract Type getType(int source);

	protected abstract int getType(Type source);

	protected abstract Severity getSeverity(int source);

	protected abstract int getSeverity(Severity source);

}
