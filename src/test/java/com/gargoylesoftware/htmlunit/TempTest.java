package com.gargoylesoftware.htmlunit;

//test for http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5003431
class TempTest {
    static class SomeType<T> { T t = null; }

    static <T> T nil() { return (new SomeType<T>()).t; }

    public static void test() {
        nil().getClass();
    }
}