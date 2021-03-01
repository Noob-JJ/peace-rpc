package util;

public class ByteUtils {

    public static int bytesToInt(byte[] bytes) {
        int result = 0;

        for (byte aByte : bytes) {
            result = result << 8 + aByte;
        }

        return result;
    }

    public static byte[] intToBytes(int number) {
        byte[] result = new byte[4];

        for (int i = 0; i < 4; i++) {
            result[i] = (byte) (number >>> (3 - i));
        }

        return result;
    }
}
