package org.sct.helper;

import java.net.URL;

import org.sct.SctConfigurationImpl;

public class SctContextBuilder {

    private SctConfigurationImpl instance;

    private SctContextBuilder() {
        instance = new SctConfigurationImpl();
    }

    public static SctContextBuilder defaults() {
        return new SctContextBuilder();
    }

    public SctContextBuilder recording(boolean b) {
        instance.setCallRecording(b);
        return this;
    }

    public SctContextBuilder recordingUrl(URL u) {
        instance.setCallRecordingUrl(u);
        return this;
    }

    public SctContextBuilder responseLoading(boolean b) {
        instance.setResponseLoading(b);
        return this;
    }

    public SctContextBuilder responseLoadingUrl(URL u) {
        instance.setResponseLoadingUrl(u);
        return this;
    }

    public SctConfigurationImpl create() {
        return instance;
    }

}
