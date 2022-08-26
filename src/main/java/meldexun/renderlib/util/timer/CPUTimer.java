package meldexun.renderlib.util.timer;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class CPUTimer extends Timer {

	private final long[] results;
	private long start;

	public CPUTimer(String name, int maxResultCount) {
		super(name);
		this.results = new long[maxResultCount];
	}

	@Override
	protected void updateInternal() {
		this.results[this.frame % this.results.length] = 0;
	}

	@Override
	protected void startInternal() {
		this.start = System.nanoTime();
	}

	@Override
	protected void stopInternal() {
		this.results[this.frame % this.results.length] += System.nanoTime() - this.start;
	}

	@Override
	public LongStream results() {
		return IntStream.range(0, this.results.length)
				.filter(i -> i != this.frame % this.results.length)
				.mapToLong(i -> this.results[i]);
	}

}
