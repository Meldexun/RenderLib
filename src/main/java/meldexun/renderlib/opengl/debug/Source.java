package meldexun.renderlib.opengl.debug;

import java.util.Arrays;

public enum Source {

	ANY("Any"),
	API("API"),
	WINDOW_SYSTEM("Window System"),
	SHADER_COMPILER("Shader Compilation"),
	THIRD_PARTY("Third Party"),
	APPLICATION("Application"),
	OTHER("Other");

	public static final String NAMES = Arrays.toString(Arrays.stream(Source.values()).map(Enum::name).toArray(String[]::new));
	private final String name;

	private Source(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
