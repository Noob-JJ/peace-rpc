package util.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by JackJ on 2021/1/21.
 */
public class KryoUtils implements Serialize{

    public static final ThreadLocal<Kryo> kryoStore = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return new Kryo();
        }
    };

    public byte[] serialize(Object object) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Output output = new Output(outputStream)) {
            Kryo kryo = kryoStore.get();
            kryo.writeObject(output, object);
            kryoStore.remove();
            output.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T deserialize(Class<T> tClass, byte[] data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             Input input = new Input(inputStream)) {
            Kryo kryo = kryoStore.get();
            input.close();
            T result = kryo.readObject(input, tClass);
            kryoStore.remove();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
