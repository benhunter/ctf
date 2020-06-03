package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;

public interface ThemeResolver {
  String resolveThemeName(HttpServletRequest paramHttpServletRequest);
  
  void setThemeName(HttpServletRequest paramHttpServletRequest, @Nullable HttpServletResponse paramHttpServletResponse, @Nullable String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/ThemeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */