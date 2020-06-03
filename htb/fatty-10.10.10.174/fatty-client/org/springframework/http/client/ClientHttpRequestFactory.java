package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;

@FunctionalInterface
public interface ClientHttpRequestFactory {
  ClientHttpRequest createRequest(URI paramURI, HttpMethod paramHttpMethod) throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/ClientHttpRequestFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */