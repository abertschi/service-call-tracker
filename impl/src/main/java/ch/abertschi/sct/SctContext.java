package ch.abertschi.sct;

import ch.abertschi.sct.api.SctConfiguration;

/**
 * Context which centralizes common used objects.
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
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
