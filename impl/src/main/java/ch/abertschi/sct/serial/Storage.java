package ch.abertschi.sct.serial;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abertschi on 12/05/16.
 */
@XStreamAlias("storage")
public class Storage
{
    public List<Call> getCalls()
    {
        return calls;
    }

    public Storage setCalls(List<Call> calls)
    {
        this.calls = calls;
        return this;
    }

    private List<Call> calls = new ArrayList<>();

    public Storage()
    {
        Call call = new Call();
        calls.add(call);
    }

    public static void main(String[] args)
    {
        //System.out.println(XStream.GET.createXStream().toXML(new Storage()));
    }
}
