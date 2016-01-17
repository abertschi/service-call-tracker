package org.sct.helper;

import java.io.File;

public class Snippets {

    private static final String ROOT = "/src/test/resources/com/axa/ch/servicemock/core" + File.separator ;
    private static final String TESTCLASS = "servicecall_mocker_test".toLowerCase() + File.separator ;
    private static final String TEST_CLASS_ROOT = ROOT + TESTCLASS + File.separator ;
    
    /**
     * Aus relativer Pfadangabe absolute Datei
     */
    private void howToFileLoad() {
        final String recordingDataset = TEST_CLASS_ROOT + "record-call.xml";
        File root = new File("");
        String path = root.getAbsolutePath() + recordingDataset;
        File recordingFile = new File(path);
        if (recordingFile.exists()) {
            recordingFile.delete();
        }
    }

}
