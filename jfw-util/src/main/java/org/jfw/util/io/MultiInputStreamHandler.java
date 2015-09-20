package org.jfw.util.io;

import java.io.IOException;
import java.io.InputStream;

public interface MultiInputStreamHandler<T> {
    void handle(InputStream in) throws IOException;
    T get();
}
