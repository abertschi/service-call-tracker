package ch.abertschi.sct.util;

import java.util.Iterator;

public interface ErrorContext {

    void add(String name, String information);

    void set(String name, String information);

    String get(String errorKey);

    Iterator<String>  keys();

}
