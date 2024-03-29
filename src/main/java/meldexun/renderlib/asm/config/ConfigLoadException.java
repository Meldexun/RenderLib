package meldexun.renderlib.asm.config;

class ConfigLoadException extends RuntimeException {

	private static final long serialVersionUID = 6858846391463366352L;

	public ConfigLoadException() {
		super();
	}

	public ConfigLoadException(String message) {
		super(message);
	}

	public ConfigLoadException(Throwable cause) {
		super(cause);
	}

	public ConfigLoadException(String message, Throwable cause) {
		super(message, cause);
	}

}
