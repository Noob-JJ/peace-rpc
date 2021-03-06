import service.Test;
import start.Rpc;

public class Main {

    public static void main(String[] args) throws Exception {
        Rpc rpc = Rpc.getInstance();

        Test t = rpc.getBean(Test.class);

    }

}
