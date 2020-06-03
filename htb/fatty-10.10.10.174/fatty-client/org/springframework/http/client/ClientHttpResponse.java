package org.springframework.http.client;

import java.io.Closeable;
import java.io.IOException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;

public interface ClientHttpResponse extends HttpInputMessage, Closeable {
  HttpStatus getStatusCode() throws IOException;
  
  int getRawStatusCode() throws IOException;
  
  String getStatusText() throws IOException;
  
  void close();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/ClientHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */