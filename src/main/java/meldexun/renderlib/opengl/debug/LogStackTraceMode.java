package meldexun.renderlib.opengl.debug;

public enum LogStackTraceMode {

	DISABLED {
		@Override
		public boolean shouldLogStackTrace(Source source, Type type, Severity severity, int id) {
			return false;
		}
	},
	ALWAYS {
		@Override
		public boolean shouldLogStackTrace(Source source, Type type, Severity severity, int id) {
			return true;
		}
	},
	ERRORS_ONLY {
		@Override
		public boolean shouldLogStackTrace(Source source, Type type, Severity severity, int id) {
			return type == Type.ERROR;
		}
	};

	public abstract boolean shouldLogStackTrace(Source source, Type type, Severity severity, int id);

}
