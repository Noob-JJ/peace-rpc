package remote.handler;

import common.RpcConstant;
import org.junit.jupiter.api.Test;
import remote.dto.RpcHeader;
import remote.dto.RpcMessage;
import remote.dto.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCoderTest {

    @Test
    public void testEncodeAnDecode() throws IOException {

        RpcHeader header = new RpcHeader().setMagicNum(RpcConstant.PROTOCOL_HEADER_MAGIC_NUM)
                .setVersion(RpcConstant.PROTOCOL_HEADER_VERSION)
                .setMessageType(RpcConstant.MESSAGE_TYPE_RESPONSE)
                .setCompressType(RpcConstant.DEFAULT_COMPRESS_TYPE)
                .setSerializeType(RpcConstant.DEFAULT_SERIALIZE_TYPE);
        RpcResponse response = new RpcResponse();
        RpcMessage message = new RpcMessage(header, response);

        byte[] bytes = SimpleCoder.encode(message);

        InputStream inputStream = new ByteArrayInputStream(bytes);

        RpcMessage deMessage = SimpleCoder.decode(inputStream);

        assertEquals(message.getHeader().getMessageType(), deMessage.getHeader().getMessageType());
        assertEquals(message.getHeader().getCompressType(), deMessage.getHeader().getCompressType());
        assertEquals(message.getHeader().getVersion(), deMessage.getHeader().getVersion());
        assertEquals(message.getHeader().getDataLength(), deMessage.getHeader().getDataLength());
        assertArrayEquals(message.getHeader().getMagicNum(), deMessage.getHeader().getMagicNum());

    }

}