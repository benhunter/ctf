package org.springframework.web.util;

public interface UriBuilderFactory extends UriTemplateHandler {
  UriBuilder uriString(String paramString);
  
  UriBuilder builder();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/UriBuilderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */