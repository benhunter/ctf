package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;

@FunctionalInterface
public interface ClientHttpRequestExecution {
  ClientHttpResponse execute(HttpRequest paramHttpRequest, byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/ClientHttpRequestExecution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */