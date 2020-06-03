package org.springframework.http.server.reactive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public interface ServerHttpResponse extends ReactiveHttpOutputMessage {
  boolean setStatusCode(@Nullable HttpStatus paramHttpStatus);
  
  @Nullable
  HttpStatus getStatusCode();
  
  MultiValueMap<String, ResponseCookie> getCookies();
  
  void addCookie(ResponseCookie paramResponseCookie);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ServerHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */