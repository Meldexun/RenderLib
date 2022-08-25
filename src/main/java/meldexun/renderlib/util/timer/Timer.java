package meldexun.renderlib.util.timer;

import meldexun.renderlib.config.RenderLibConfig;

public abstract class Timer implements ITimer {

	protected final String name;
	protected int frame;
	protected boolean active;

	public Timer(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void update() {
		if (RenderLibConfig.showFrameTimes) {
			this.frame++;
			this.updateInternal();
		}
	}

	@Override
	public void start() {
		if (RenderLibConfig.showFrameTimes) {
			this.active = true;
			this.startInternal();
		}
	}

	@Override
	public void stop() {
		if (this.active) {
			this.stopInternal();
			this.active = false;
		}
	}

	protected abstract void updateInternal();

	protected abstract void startInternal();

	protected abstract void stopInternal();

}
