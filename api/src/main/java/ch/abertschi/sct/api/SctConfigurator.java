package ch.abertschi.sct.api;

public class SctConfigurator {

	private static final SctConfigurator INSTANCE = new SctConfigurator();

	private SctConfiguration configuration;

	private SctConfigurator() {
	}

	public static SctConfigurator getInstance() {
		return INSTANCE;
	}

	public void setConfiguration(SctConfiguration c) {
		this.configuration = c;
	}

	public SctConfiguration getConfiguration() {
		return this.configuration;
	}

}
