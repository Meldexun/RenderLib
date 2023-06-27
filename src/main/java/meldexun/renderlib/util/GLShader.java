package meldexun.renderlib.util;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class GLShader {

	private static final IntList PROGRAM_STACK = new IntArrayList();
	private int program;
	private final FunctionalObject2IntMap<String> uniforms = new FunctionalObject2IntMap<>();
	private final FunctionalObject2IntMap<String> attributes = new FunctionalObject2IntMap<>();

	public GLShader(int program) {
		this.program = program;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static void push() {
		PROGRAM_STACK.add(GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM));
	}

	public static void pop() {
		use(PROGRAM_STACK.removeInt(PROGRAM_STACK.size() - 1));
	}

	public static void use(int program) {
		GL20.glUseProgram(program);
	}

	public int getProgram() {
		return program;
	}

	public void use() {
		use(program);
	}

	public void bind() {
		push();
		use();
	}

	public void unbind() {
		pop();
	}

	public int getUniform(String uniform) {
		return uniforms.computeIfAbsent(uniform, (ToIntFunction<String>) k -> GL20.glGetUniformLocation(program, k));
	}

	public int getAttribute(String attribute) {
		return attributes.computeIfAbsent(attribute, (ToIntFunction<String>) k -> GL20.glGetAttribLocation(program, k));
	}

	public void dispose() {
		GL20.glDeleteProgram(program);
		program = -1;
	}

	public static class Builder {

		private final Int2ObjectMap<Supplier<String>> shaderMap = new Int2ObjectOpenHashMap<>();
		private final Object2IntMap<String> attributeMap = new Object2IntOpenHashMap<>();

		public Builder addShader(int type, Supplier<String> source) {
			shaderMap.put(type, source);
			return this;
		}

		public Builder bindAttribute(String attribute, int index) {
			attributeMap.put(attribute, index);
			return this;
		}

		public GLShader build() {
			int program = GL20.glCreateProgram();

			IntList shaderList = new IntArrayList();
			for (Int2ObjectMap.Entry<Supplier<String>> entry : shaderMap.int2ObjectEntrySet()) {
				int shader = GL20.glCreateShader(entry.getIntKey());
				GL20.glShaderSource(shader, entry.getValue().get());
				GL20.glCompileShader(shader);

				int compileStatus = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
				if (compileStatus != GL11.GL_TRUE) {
					int logLength = GL20.glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH);
					String shaderInfoLog = GL20.glGetShaderInfoLog(shader, logLength);
					throw new RuntimeException(String.format("Failed to compile shader: %d%n%s", compileStatus, shaderInfoLog));
				}

				shaderList.add(shader);
			}
			shaderList.forEach(shader -> GL20.glAttachShader(program, shader));

			attributeMap.forEach((attribute, index) -> GL20.glBindAttribLocation(program, index, attribute));

			GL20.glLinkProgram(program);

			int linkStatus = GL20.glGetProgrami(program, GL20.GL_LINK_STATUS);
			if (linkStatus != GL11.GL_TRUE) {
				int logLength = GL20.glGetProgrami(program, GL20.GL_INFO_LOG_LENGTH);
				String programInfoLog = GL20.glGetProgramInfoLog(program, logLength);
				throw new RuntimeException(String.format("Failed to link program: %d%n%s", linkStatus, programInfoLog));
			}

			shaderList.forEach(GL20::glDeleteShader);

			return new GLShader(program);
		}

		public GLShader build(Consumer<GLShader> callback) {
			GLShader shader = build();
			push();
			shader.use();
			callback.accept(shader);
			pop();
			return shader;
		}

	}

}
