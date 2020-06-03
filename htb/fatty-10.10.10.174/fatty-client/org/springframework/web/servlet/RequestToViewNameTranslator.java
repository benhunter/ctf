package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

public interface RequestToViewNameTranslator {
  @Nullable
  String getViewName(HttpServletRequest paramHttpServletRequest) throws Exception;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/RequestToViewNameTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */