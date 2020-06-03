package org.springframework.web.context.request;

public interface AsyncWebRequestInterceptor extends WebRequestInterceptor {
  void afterConcurrentHandlingStarted(WebRequest paramWebRequest);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/AsyncWebRequestInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */