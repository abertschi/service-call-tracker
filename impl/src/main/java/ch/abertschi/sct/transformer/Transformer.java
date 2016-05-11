package ch.abertschi.sct.transformer;

/**
 * Created by abertschi on 11/05/16.
 */
public interface Transformer
{
    boolean canTransform(CallContext context, String input);

    String transform(CallContext context, String input);
}
