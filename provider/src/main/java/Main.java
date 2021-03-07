import service.TestService;
import start.Rpc;

public class Main {
    public static void main(String[] args) {
        new Rpc().group("1.0").version("1.0").registry(TestService.class).start();

    }
}
