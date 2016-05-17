package ch.abertschi.sct.api;

import java.io.File;
import java.net.URL;

/**
 * Created by abertschi on 14/05/16.
 */
public class SctConfigurationImpl
{

    private static final String CONFIG_SKIP = "sct.skip";
    private static final String CONFIG_EXCEPTION_ON_NOT_FOUND = "sct.exception-on-not-found";
    private static final String CONFIG_IGNORE_UNKNOWN_FIELDS = "sct.ignore-unknown-fields";

    private static final String CONFIG_RECORD_ENABLE = "sct.record.enable";
    private static final String CONFIG_RECORD_FILE = "sct.record.file";
    private static final String CONFIG_RECORD_DIRECTORY = "sct.record.directory";
    private static final String CONFIG_RECORD_FILE_FOR_EACH_CALL = "sct.record.file-for-each-call";
    private static final String CONFIG_RECORD_MODE = "sct.record.mode";
    private static final String CONFIG_RECORD_SKIP_DOUBLE = "sct.record.skip-doubles";

    private static final String CONFIG_REPLAY_ENABLE = "sct.replay.enable";
    private static final String CONFIG_REPLAY_FILE = "sct.replay.file";
    private static final String CONFIG_REPLAY_DIRECTORY = "sct.replay.directory";
    private static final String CONFIG_REPLAY_FILE_FOR_EACH_CALL = "sct.replay.file-for-each-call";

    // general
    private boolean skip;
    private boolean exceptionOnNotFound;
    private boolean ignoreUnknownFields;

    // recording
    private boolean recordingEnabled;
    private File recordingFile;
    private File recordingDirectory;
    private boolean recordingFileForEachCall;
    private boolean recordingSkipDoubles;
    private RECORDING_MODE recordingMode;

    public enum RECORDING_MODE
    {
        DUMP_CALLS, OVERWRITE
    }

    // replaying
    private boolean replayingEnabled;
    private File replayingFile;
    private File replayingDirectory;
    private boolean replayingFileForEachCall;

    public SctConfigurationImpl()
    {

    }

    private String getOverwrite(String name)
    {
        return System.getProperty(name);
    }

    private boolean hasOverwrite(String name)
    {
        String prop = System.getProperty(name);
        return prop != null && !prop.isEmpty();
    }

    private boolean getBoolSystemPropertyOrValue(String key, boolean value)
    {
        return hasOverwrite(key) ?
                "true".equals(getOverwrite(key).toLowerCase()) :
                value;
    }


    public boolean isExceptionOnNotFound()
    {
        return getBoolSystemPropertyOrValue(CONFIG_EXCEPTION_ON_NOT_FOUND, exceptionOnNotFound);
    }

    public SctConfigurationImpl setExceptionOnNotFound(boolean exceptionOnNotFound)
    {
        this.exceptionOnNotFound = exceptionOnNotFound;
        return this;
    }

    public boolean isIgnoreUnknownFields()
    {
        return getBoolSystemPropertyOrValue(CONFIG_IGNORE_UNKNOWN_FIELDS, ignoreUnknownFields);
    }

    public SctConfigurationImpl setIgnoreUnknownFields(boolean ignoreUnknownFields)
    {
        this.ignoreUnknownFields = ignoreUnknownFields;
        return this;
    }

    public File getRecordingDirectory()
    {
        return hasOverwrite(CONFIG_RECORD_DIRECTORY) ?
                new File(CONFIG_RECORD_DIRECTORY) :
                recordingDirectory;
    }

    public SctConfigurationImpl setRecordingDirectory(File recordingDirectory)
    {
        this.recordingDirectory = recordingDirectory;
        return this;
    }

    public boolean isRecordingEnabled()
    {
        return getBoolSystemPropertyOrValue(CONFIG_RECORD_ENABLE, recordingEnabled);
    }

    public SctConfigurationImpl setRecordingEnabled(boolean recordingEnabled)
    {
        this.recordingEnabled = recordingEnabled;
        return this;
    }

    public File getRecordingFile()
    {
        return hasOverwrite(CONFIG_RECORD_FILE) ?
                new File(getOverwrite(CONFIG_RECORD_FILE)) :
                recordingFile;
    }

    public SctConfigurationImpl setRecordingFile(File recordingFile)
    {
        this.recordingFile = recordingFile;
        return this;
    }

    public boolean isRecordingFileForEachCall()
    {
        return getBoolSystemPropertyOrValue(CONFIG_RECORD_FILE_FOR_EACH_CALL, recordingFileForEachCall);
    }

    public SctConfigurationImpl setRecordingFileForEachCall(boolean recordingFileForEachCall)
    {
        this.recordingFileForEachCall = recordingFileForEachCall;
        return this;
    }

    public RECORDING_MODE getRecordingMode()
    {
        String overwrite = getOverwrite(CONFIG_RECORD_MODE);
        return "overwrite".equals(overwrite) ? RECORDING_MODE.OVERWRITE :
                "dump".equals(overwrite) ? RECORDING_MODE.DUMP_CALLS :
                        recordingMode;
    }

    public SctConfigurationImpl setRecordingMode(RECORDING_MODE recordingMode)
    {
        this.recordingMode = recordingMode;
        return this;
    }

    public boolean isRecordingSkipDoubles()
    {
        return getBoolSystemPropertyOrValue(CONFIG_RECORD_SKIP_DOUBLE, recordingSkipDoubles);
    }

    public SctConfigurationImpl setRecordingSkipDoubles(boolean recordingSkipDoubles)
    {
        this.recordingSkipDoubles = recordingSkipDoubles;
        return this;
    }

    public File getReplayingDirectory()
    {
        return hasOverwrite(CONFIG_REPLAY_DIRECTORY) ?
                new File(getOverwrite(CONFIG_REPLAY_DIRECTORY)) :
                replayingDirectory;
    }

    public SctConfigurationImpl setReplayingDirectory(File replayingDirectory)
    {
        this.replayingDirectory = replayingDirectory;
        return this;
    }

    public boolean isReplayingEnabled()
    {
        return getBoolSystemPropertyOrValue(CONFIG_REPLAY_ENABLE, replayingEnabled);
    }

    public SctConfigurationImpl setReplayingEnabled(boolean replayingEnabled)
    {
        this.replayingEnabled = replayingEnabled;
        return this;
    }

    public File getReplayingFile()
    {
        return hasOverwrite(CONFIG_REPLAY_FILE) ?
                new File(getOverwrite(CONFIG_REPLAY_FILE)) :
                replayingFile;
    }

    public SctConfigurationImpl setReplayingFile(File replayingFile)
    {
        this.replayingFile = replayingFile;
        return this;
    }

    public boolean isReplayingFileForEachCall()
    {
        return getBoolSystemPropertyOrValue(CONFIG_REPLAY_FILE_FOR_EACH_CALL, replayingFileForEachCall);
    }

    public SctConfigurationImpl setReplayingFileForEachCall(boolean replayingFileForEachCall)
    {
        this.replayingFileForEachCall = replayingFileForEachCall;
        return this;
    }

    public boolean isSkip()
    {
        return getBoolSystemPropertyOrValue(CONFIG_SKIP, skip);
    }

    public SctConfigurationImpl setSkip(boolean skip)
    {
        this.skip = skip;
        return this;
    }


}
