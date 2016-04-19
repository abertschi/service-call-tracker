package ch.abertschi.sct.api;

import java.net.URL;

public interface SctConfiguration {

	boolean isCallRecording();

	boolean isResponseLoading();

	URL getCallRecordingUrl();

	URL getResponseLoadingUrl();
}
	