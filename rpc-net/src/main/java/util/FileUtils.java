package util;

import java.net.URL;

/**
 * Created by JackJ on 2021/1/17.
 */
public class FileUtils {


    public static URL loadFile(String fileName){
        return Thread.currentThread().getContextClassLoader().getResource(fileName);
    }

}
