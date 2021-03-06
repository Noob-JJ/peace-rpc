package service;

import exception.TestException;

public class TestService implements Test{

    @Override
    public void noParam() {
        System.out.println("this is a no param test");
    }

    @Override
    public String hasReturn(int integer) {
        return integer + "success";
    }

    @Override
    public void hasException() {
        throw new TestException();
    }

    @Override
    public void noReturn(String string) {
        System.out.println(string);
    }
}
