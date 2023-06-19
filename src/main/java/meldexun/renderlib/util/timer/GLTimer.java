package meldexun.renderlib.util.timer;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.lwjgl.opengl.ARBTimerQuery;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;

import meldexun.renderlib.util.GLQueryResult;
import meldexun.renderlib.util.GLUtil;
import meldexun.renderlib.util.Pair;

public class GLTimer extends Timer {

	private final Frame[] results;
	private GLQueryResult start;

	public GLTimer(String name, int maxResultCount) {
		super(name, maxResultCount);
		this.results = IntStream.range(0, maxResultCount)
				.mapToObj(i -> new Frame())
				.toArray(Frame[]::new);
	}

	@Override
	protected void updateInternal() {
		this.results[this.frame].reset();
	}

	@Override
	protected void startInternal() {
		this.start = timestamp();
	}

	@Override
	protected void stopInternal() {
		this.results[this.frame].addPending(this.start, timestamp());
	}

	@Override
	public LongStream results() {
		return IntStream.range(0, this.results.length)
				.filter(i -> i != this.frame)
				.mapToObj(i -> this.results[i])
				.filter(Frame::ready)
				.mapToLong(Frame::result);
	}

	private static GLQueryResult timestamp() {
		int q = GL15.glGenQueries();
		if (GLUtil.CAPS.OpenGL33) {
			GL33.glQueryCounter(q, GL33.GL_TIMESTAMP);
		} else if (GLUtil.CAPS.GL_ARB_timer_query) {
			ARBTimerQuery.glQueryCounter(q, ARBTimerQuery.GL_TIMESTAMP);
		} else {
			throw new UnsupportedOperationException();
		}
		return new GLQueryResult(q);
	}

	private static class Frame {

		private final List<Pair<GLQueryResult, GLQueryResult>> pending = new LinkedList<>();
		private long time;

		public void addPending(GLQueryResult start, GLQueryResult end) {
			this.pending.add(new Pair<>(start, end));
		}

		public void reset() {
			this.pending.forEach(pair -> {
				pair.getLeft().dispose();
				pair.getRight().dispose();
			});
			this.pending.clear();
			this.time = 0;
		}

		public void checkPending() {
			this.pending.removeIf(pair -> {
				if (pair.getLeft().ready() && pair.getRight().ready()) {
					this.time += pair.getRight().time() - pair.getLeft().time();
					pair.getLeft().dispose();
					pair.getRight().dispose();
					return true;
				}
				return false;
			});
		}

		public boolean ready() {
			this.checkPending();
			return this.pending.isEmpty();
		}

		public long result() {
			return this.time;
		}

	}

}
