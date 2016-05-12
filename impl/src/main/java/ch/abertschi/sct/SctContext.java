package ch.abertschi.sct;

import ch.abertschi.sct.api.SctConfiguration;

public class SctContext {

    private SctConfiguration configuration;

    public SctContext() {
    }

    public SctConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SctConfiguration configuration) {
        this.configuration = configuration;
    }
}
