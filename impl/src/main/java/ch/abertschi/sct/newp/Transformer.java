package ch.abertschi.sct.newp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by abertschi on 11/05/16.
 */
public interface Transformer
{
    boolean canTransform(TransformingContext context, String input);

    String transform(TransformingContext context, String input);
}
