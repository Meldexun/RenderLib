package meldexun.renderlib.opengl.debug;

import java.util.Arrays;

public enum Severity {

	ANY("Any"),
	HIGH("High"),
	MEDIUM("Medium"),
	LOW("Low"),
	NOTIFICATION("Notification");

	public static final String NAMES = Arrays.toString(Arrays.stream(Severity.values()).map(Enum::name).toArray(String[]::new));
	private final String name;

	private Severity(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
