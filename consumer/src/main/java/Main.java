import service.Test;
import start.Rpc;

public class Main {

    public static void main(String[] args) {
        Test test = new Rpc().group("1.0").version("1.0").getBean(Test.class);

        System.out.println(test.hasReturn(123));

    }

}
