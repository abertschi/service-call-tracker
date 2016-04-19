package ch.abertschi.sct.xstream;

import java.util.List;

import ch.abertschi.sct.call.CallObject;
import ch.abertschi.sct.xstream.exclusion.FieldExclusionWriterMatcher;
import com.thoughtworks.xstream.io.path.Path;

/**
 * {@link FieldExclusionWriterMatcher} implementation
 * 
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public class FieldExclusionWriterMatcherImpl implements FieldExclusionWriterMatcher {

   /**
    * The {@link Path} of an unmarshaled and marshalled {@link CallObject} is different.
    * This matcher compares unmarshaled and marshaled {@link CallObject} correctly.
    */
    @Override
    public boolean doesExclusionMatchForCurrent(Path path, List<Path> exclusions) {
        if (exclusions != null) {
            String current = path.toString();
            current = current.replaceFirst("/object/", "/");
            for (Path exclusion : exclusions) {
                if (current.contains(exclusion.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

}
