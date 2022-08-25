package meldexun.renderlib.util.timer;

import java.util.stream.LongStream;

public class DummyTimer implements ITimer {

	private final String name;

	public DummyTimer(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void update() {

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public LongStream results() {
		return LongStream.empty();
	}

	@Override
	public String avgString() {
		return "?";
	}

	@Override
	public String minString() {
		return "?";
	}

	@Override
	public String maxString() {
		return "?";
	}

}
