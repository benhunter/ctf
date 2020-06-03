package org.springframework.web.servlet;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;

public interface LocaleResolver {
  Locale resolveLocale(HttpServletRequest paramHttpServletRequest);
  
  void setLocale(HttpServletRequest paramHttpServletRequest, @Nullable HttpServletResponse paramHttpServletResponse, @Nullable Locale paramLocale);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/LocaleResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */