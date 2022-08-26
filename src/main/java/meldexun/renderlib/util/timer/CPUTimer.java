package meldexun.renderlib.util.timer;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class CPUTimer extends Timer {

	private final long[] results;
	private long start;

	public CPUTimer(String name, int maxResultCount) {
		super(name, maxResultCount);
		this.results = new long[maxResultCount];
	}

	@Override
	protected void updateInternal() {
		this.results[this.frame] = 0;
	}

	@Override
	protected void startInternal() {
		this.start = System.nanoTime();
	}

	@Override
	protected void stopInternal() {
		this.results[this.frame] += System.nanoTime() - this.start;
	}

	@Override
	public LongStream results() {
		return IntStream.range(0, this.results.length)
				.filter(i -> i != this.frame)
				.mapToLong(i -> this.results[i]);
	}

}
