package ch.abertschi.sct.parse;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class ParserContext
{
    private List<ParserCall> calls = new LinkedList<>();

    public List<ParserCall> getCalls()
    {
        return calls;
    }

    public ParserContext setCalls(List<ParserCall> calls)
    {
        this.calls = calls;
        return this;
    }
}
