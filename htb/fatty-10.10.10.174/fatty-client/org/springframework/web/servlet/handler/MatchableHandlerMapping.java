package org.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerMapping;

public interface MatchableHandlerMapping extends HandlerMapping {
  @Nullable
  RequestMatchResult match(HttpServletRequest paramHttpServletRequest, String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/MatchableHandlerMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */