package ch.abertschi.sct.newp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class StorageContext
{
    private List<StorageCall> calls = new LinkedList<>();

    public List<StorageCall> getCalls()
    {
        return calls;
    }

    public StorageContext setCalls(List<StorageCall> calls)
    {
        this.calls = calls;
        return this;
    }
}
