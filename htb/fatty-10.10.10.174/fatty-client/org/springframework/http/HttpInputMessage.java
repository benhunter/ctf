package org.springframework.http;

import java.io.IOException;
import java.io.InputStream;

public interface HttpInputMessage extends HttpMessage {
  InputStream getBody() throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/HttpInputMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */