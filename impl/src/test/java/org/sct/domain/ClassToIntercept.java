package org.sct.domain;


public class ClassToIntercept {

    public <T> T returnObject(T given) {
        return given;
    }

}
