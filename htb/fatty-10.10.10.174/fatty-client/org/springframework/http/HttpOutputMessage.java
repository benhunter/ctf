package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpOutputMessage extends HttpMessage {
  OutputStream getBody() throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/HttpOutputMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */