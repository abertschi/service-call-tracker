package ch.abertschi.sct;

/**
 * Created by abertschi on 11/05/16.
 */
public interface CallCollectable
{
    Object get(Object request) throws Throwable;

    void put(Object request, Object response);
}
