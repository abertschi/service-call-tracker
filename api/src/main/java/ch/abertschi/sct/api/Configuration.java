package ch.abertschi.sct.api;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * Created by abertschi on 17/05/16.
 */
public class Configuration
{
    // general
    private boolean throwExceptionOnNotFound = false;
    private boolean throwExceptionOnIncompatibleReturnType = true;
    //private boolean ignoreUnknownFields = false;

    // recording
    private boolean recordingEnabled = false;
    private File recordingSource;
    private INPUT_SOURCE recordingSourceType = INPUT_SOURCE.SINGLE_FILE;
    private boolean recordingSkipDoubles = true;
    private RECORDING_MODE recordingMode = RECORDING_MODE.OVERWRITE;


    public enum RECORDING_MODE
    {
        DUMP_CALLS, OVERWRITE
    }

    public enum INPUT_SOURCE
    {
        SINGLE_FILE, DIRECTORY
    }

    // replaying
    private boolean replayingEnabled = false;
    private URL replayingSource;
    private INPUT_SOURCE replayingSourceType = INPUT_SOURCE.SINGLE_FILE;

    public Configuration()
    {
    }

    public boolean isRecordingEnabled()
    {
        return recordingEnabled;
    }

    public Configuration setRecordingEnabled(boolean recordingEnabled)
    {
        this.recordingEnabled = recordingEnabled;
        return this;
    }

    public RECORDING_MODE getRecordingMode()
    {
        return recordingMode;
    }

    public Configuration setRecordingMode(RECORDING_MODE recordingMode)
    {
        this.recordingMode = recordingMode;
        return this;
    }

    public boolean isRecordingSkipDoubles()
    {
        return recordingSkipDoubles;
    }

    public Configuration setRecordingSkipDoubles(boolean recordingSkipDoubles)
    {
        this.recordingSkipDoubles = recordingSkipDoubles;
        return this;
    }

    public File getRecordingSource()
    {
        return recordingSource;
    }

    public Configuration setRecordingSource(File recordingSource)
    {
        this.recordingSource = recordingSource;
        return this;
    }

    public INPUT_SOURCE getRecordingSourceType()
    {
        return recordingSourceType;
    }

    public Configuration setRecordingSourceType(INPUT_SOURCE recordingSourceType)
    {
        this.recordingSourceType = recordingSourceType;
        return this;
    }

    public boolean isReplayingEnabled()
    {
        return replayingEnabled;
    }

    public Configuration setReplayingEnabled(boolean replayingEnabled)
    {
        this.replayingEnabled = replayingEnabled;
        return this;
    }

    public URL getReplayingSource()
    {
        return replayingSource;
    }

    public Configuration setReplayingSource(URL replayingSource)
    {
        this.replayingSource = replayingSource;
        return this;
    }

    public INPUT_SOURCE getReplayingSourceType()
    {
        return replayingSourceType;
    }

    public Configuration setReplayingSourceType(INPUT_SOURCE replayingSourceType)
    {
        this.replayingSourceType = replayingSourceType;
        return this;
    }

    public boolean isThrowExceptionOnNotFound()
    {
        return throwExceptionOnNotFound;
    }

    public Configuration setThrowExceptionOnNotFound(boolean throwExceptionOnNotFound)
    {
        this.throwExceptionOnNotFound = throwExceptionOnNotFound;
        return this;
    }

    public boolean isThrowExceptionOnIncompatibleReturnType()
    {
        return throwExceptionOnIncompatibleReturnType;
    }

    public Configuration setThrowExceptionOnIncompatibleReturnType(boolean throwExceptionOnIncompatibleReturnType)
    {
        this.throwExceptionOnIncompatibleReturnType = throwExceptionOnIncompatibleReturnType;
        return this;
    }

//    public static ConfigurationFactory createRecordingConfiguration(File recordingFile)
//    {
//        ConfigurationFactory factory = new ConfigurationFactory();
//        factory.set(true)
//                .setRecordingFile(recordingFile);
//        return factory;
//
//    }
//
//
//    public static ConfigurationFactory createRecordingAndReplayingConfiguration(File file)
//    {
//        ConfigurationFactory factory = new ConfigurationFactory();
//        factory.setRecordingEnabled(true)
//                .setRecordingFile(file)
//                .setReplayingEnabled(true)
//                .setReplayingFile(file);
//        return factory;
//    }
//
//    public static ConfigurationFactory crateReplayingConfiguration(File replayingFile)
//    {
//        ConfigurationFactory factory = new ConfigurationFactory();
//        factory
//                .setReplayingEnabled(true)
//                .setReplayingFile(replayingFile);
//        return factory;
//    }

}
