package org.springframework.http.client.reactive;

import java.net.URI;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.util.MultiValueMap;

public interface ClientHttpRequest extends ReactiveHttpOutputMessage {
  HttpMethod getMethod();
  
  URI getURI();
  
  MultiValueMap<String, HttpCookie> getCookies();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/ClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */