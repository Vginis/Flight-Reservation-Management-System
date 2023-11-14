package org.acme.util;

import java.time.LocalDate;

public class SystemDate {

    protected SystemDate() { }

    private static LocalDate stub;



    protected static void setStub(LocalDate stubDate) {
        stub = stubDate;
    }


    protected static void removeStub() {
        stub = null;
    }


    public static LocalDate now() {
        return stub == null ? LocalDate.now() : stub;
    }
}
