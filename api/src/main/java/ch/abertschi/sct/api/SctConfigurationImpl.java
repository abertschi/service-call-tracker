package ch.abertschi.sct.api;

import java.net.URL;

/**
 * Created by abertschi on 14/05/16.
 */
public class SctConfigurationImpl implements SctConfiguration
{
    private boolean isCallRecording;
    private boolean isResponseLoading;
    private URL callRecordingUrl;
    private URL responseLoadingUrl;
    private boolean isEnabled;

    @Override
    public boolean isCallRecording()
    {
        return false;
    }

    @Override
    public boolean isResponseLoading()
    {
        return false;
    }

    @Override
    public URL getCallRecordingUrl()
    {
        return null;
    }

    @Override
    public URL getResponseLoadingUrl()
    {
        return null;
    }
}
