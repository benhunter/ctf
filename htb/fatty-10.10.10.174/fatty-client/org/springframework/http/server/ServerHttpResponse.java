package org.springframework.http.server;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;

public interface ServerHttpResponse extends HttpOutputMessage, Flushable, Closeable {
  void setStatusCode(HttpStatus paramHttpStatus);
  
  void flush() throws IOException;
  
  void close();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/ServerHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */