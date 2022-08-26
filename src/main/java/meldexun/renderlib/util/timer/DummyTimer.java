package meldexun.renderlib.util.timer;

import java.util.stream.LongStream;

public class DummyTimer implements ITimer {

	private final String name;
	private final String value;

	public DummyTimer(String name, String value) {
		this.name = name;
		this.value = value;
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
		return this.value;
	}

	@Override
	public String minString() {
		return this.value;
	}

	@Override
	public String maxString() {
		return this.value;
	}

}
