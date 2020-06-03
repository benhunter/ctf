package org.springframework.core.serializer;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface Deserializer<T> {
  T deserialize(InputStream paramInputStream) throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/serializer/Deserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */