package ch.abertschi.sct.newp.transformer;

/**
 * Created by abertschi on 11/05/16.
 */
public interface Transformer
{
    boolean canTransform(CallContext context, String input);

    String transform(CallContext context, String input);
}
