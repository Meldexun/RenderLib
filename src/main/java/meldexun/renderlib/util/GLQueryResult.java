package meldexun.renderlib.util;

import org.lwjgl.opengl.ARBTimerQuery;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;

public class GLQueryResult {

	private int query;
	private long time;

	public GLQueryResult(int query) {
		this.query = query;
	}

	public void dispose() {
		if (this.query != -1) {
			GL15.glDeleteQueries(this.query);
			this.query = -1;
		}
	}

	public boolean ready() {
		if (this.query != -1) {
			if (GL15.glGetQueryObjecti(this.query, GL15.GL_QUERY_RESULT_AVAILABLE) == GL11.GL_TRUE) {
				if (GLUtil.CAPS.OpenGL33) {
					this.time = GL33.glGetQueryObjecti64(this.query, GL15.GL_QUERY_RESULT);
				} else if (GLUtil.CAPS.GL_ARB_timer_query) {
					this.time = ARBTimerQuery.glGetQueryObjecti64(this.query, GL15.GL_QUERY_RESULT);
				} else {
					throw new UnsupportedOperationException();
				}
				GL15.glDeleteQueries(this.query);
				this.query = -1;
			}
		}
		return this.query == -1;
	}

	public long time() {
		return this.time;
	}

}
