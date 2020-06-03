package org.springframework.core.serializer;

import java.io.IOException;
import java.io.OutputStream;

@FunctionalInterface
public interface Serializer<T> {
  void serialize(T paramT, OutputStream paramOutputStream) throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/serializer/Serializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */