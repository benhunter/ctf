package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamSource {
  InputStream getInputStream() throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/InputStreamSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */