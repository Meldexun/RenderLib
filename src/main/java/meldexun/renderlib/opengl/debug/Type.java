package meldexun.renderlib.opengl.debug;

import java.util.Arrays;

public enum Type {

	ANY("Any"),
	ERROR("Error"),
	DEPRECATED_BEHAVIOR("Deprecated Behavior"),
	UNDEFINED_BEHAVIOR("Undefined Behavior"),
	PORTABILITY("Portability"),
	PERFORMANCE("Performance"),
	MARKER("Marker"),
	PUSH_GROUP("Push Group"),
	POP_GROUP("Pop Group"),
	OTHER("Other");

	public static final String NAMES = Arrays.toString(Arrays.stream(Type.values()).map(Enum::name).toArray(String[]::new));
	private final String name;

	private Type(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
