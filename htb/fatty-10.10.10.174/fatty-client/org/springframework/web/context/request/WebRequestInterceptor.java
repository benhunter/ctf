package org.springframework.web.context.request;

import org.springframework.lang.Nullable;
import org.springframework.ui.ModelMap;

public interface WebRequestInterceptor {
  void preHandle(WebRequest paramWebRequest) throws Exception;
  
  void postHandle(WebRequest paramWebRequest, @Nullable ModelMap paramModelMap) throws Exception;
  
  void afterCompletion(WebRequest paramWebRequest, @Nullable Exception paramException) throws Exception;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/WebRequestInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */