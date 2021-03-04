package service;

/**
 * Created by JackJ on 2021/1/17.
 */
public interface Test {


    void noParam();

    void noReturn(String string);

    String hasReturn(int integer);

    void hasException() throws Exception;
}
