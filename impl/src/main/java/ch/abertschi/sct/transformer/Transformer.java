package ch.abertschi.sct.transformer;

/**
 * Created by abertschi on 11/05/16.
 */
public interface Transformer
{
    boolean canTransform(TransformerContext context, String input);

    String transform(TransformerContext context, String input);
}
