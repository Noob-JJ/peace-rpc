package util.serialize;

public class SerializeFactory {


    public static Serialize getSerializeUtil() {

        return new KryoUtils();
    }
}
