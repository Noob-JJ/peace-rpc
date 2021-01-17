package net.server;

import net.entity.RpcResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by JackJ on 2021/1/16.
 */
public interface RequestHandler {

    void handler(InputStream inputStream, OutputStream outputStream) throws IOException;
}