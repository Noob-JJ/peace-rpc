package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static util.ByteUtils.*;

class ByteUtilsTest {

    @Test
    void testBytesToInt() {
        int testNumber = 57;

        byte[] midResult = intToBytes(testNumber);
        int result = bytesToInt(midResult);

        assertEquals(testNumber, result);
    }
}