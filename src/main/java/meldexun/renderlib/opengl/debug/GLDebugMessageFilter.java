package meldexun.renderlib.opengl.debug;

public class GLDebugMessageFilter {

	public Source source;
	public Type type;
	public Severity severity;
	public boolean enabled;

	public GLDebugMessageFilter() {
		this(Source.ANY, Type.ANY, Severity.ANY, true);
	}

	public GLDebugMessageFilter(Source source, Type type, Severity severity, boolean enabled) {
		this.source = source;
		this.type = type;
		this.severity = severity;
		this.enabled = enabled;
	}

	public GLDebugMessageFilter(String s) {
		String[] a = s.split(";");
		if (a.length != 4) {
			throw new IllegalArgumentException();
		}
		this.source = Source.valueOf(a[0].trim());
		this.type = Type.valueOf(a[1].trim());
		this.severity = Severity.valueOf(a[2].trim());
		this.enabled = Boolean.parseBoolean(a[3].trim());
	}

	@Override
	public String toString() {
		return String.format("%s;%s;%s;%b", source.name(), type.name(), severity.name(), enabled);
	}

}
