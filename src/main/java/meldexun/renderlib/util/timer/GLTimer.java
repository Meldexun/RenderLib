package meldexun.renderlib.util.timer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;

import meldexun.renderlib.util.GLQueryResult;
import meldexun.renderlib.util.Pair;

public class GLTimer extends Timer {

	private final Frame[] results;
	private GLQueryResult start;

	public GLTimer(String name, int maxResultCount) {
		super(name);
		this.results = IntStream.range(0, maxResultCount).mapToObj(i -> new Frame()).toArray(Frame[]::new);
	}

	@Override
	public void updateInternal() {
		this.results[this.frame % this.results.length].reset();
	}

	@Override
	protected void startInternal() {
		this.start = timestamp();
	}

	@Override
	protected void stopInternal() {
		this.results[this.frame % this.results.length].addPending(this.start, timestamp());
	}

	@Override
	public LongStream results() {
		Stream<Frame> s;
		if (this.active) {
			int j = this.frame % this.results.length;
			s = IntStream.range(0, this.results.length).filter(i -> i != j).mapToObj(i -> this.results[i]);
		} else {
			s = Arrays.stream(this.results);
		}
		return s.filter(Frame::ready).mapToLong(Frame::result);
	}

	private static GLQueryResult timestamp() {
		int q = GL15.glGenQueries();
		GL33.glQueryCounter(q, GL33.GL_TIMESTAMP);
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
