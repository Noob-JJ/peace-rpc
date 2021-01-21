package util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by JackJ on 2021/1/21.
 */
public class KryoUtils {

    public static final ThreadLocal<Kryo> kryoStore = new ThreadLocal<Kryo>(){
        @Override
        protected Kryo initialValue() {
            return new Kryo();
        }
    };

    public static byte[] serialize(Object object) {
        Kryo kryo = kryoStore.get();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, object);
        output.close();
        return outputStream.toByteArray();
    }

    public static <T> T deserialize(Class<T> tClass, byte[] data) {
        Kryo kryo = kryoStore.get();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Input input = new Input(inputStream);
        input.close();
        return (T) kryo.readClassAndObject(input);
    }
}
