package util.serialize;

public interface Serialize {

    byte[] serialize(Object object);

    <T> T deserialize(Class<T> tClass, byte[] bytes);

}
