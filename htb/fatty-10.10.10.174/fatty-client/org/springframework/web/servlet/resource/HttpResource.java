package org.springframework.web.servlet.resource;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

public interface HttpResource extends Resource {
  HttpHeaders getResponseHeaders();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/HttpResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */