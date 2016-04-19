package ch.abertschi.sct.xstream.exclusion;

import java.util.List;

import com.thoughtworks.xstream.io.path.Path;

/**
 * Exclusion matcher determining if currently
 * processed field in XML DOM should be ignored or not.
 * 
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public interface FieldExclusionWriterMatcher {

    boolean doesExclusionMatchForCurrent(Path path, List<Path> exclusions);

    /**
     * Default exclusion matcher
     */
    public static class FieldExclusionWriterMatcherDefault implements FieldExclusionWriterMatcher {

        @Override
        public boolean doesExclusionMatchForCurrent(Path path, List<Path> exclusions) {
            if (exclusions != null) {
                String current = path.toString();

                for (Path exclusion : exclusions) {
                    if (current.contains(exclusion.toString())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
