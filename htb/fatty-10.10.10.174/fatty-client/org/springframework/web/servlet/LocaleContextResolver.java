package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.lang.Nullable;

public interface LocaleContextResolver extends LocaleResolver {
  LocaleContext resolveLocaleContext(HttpServletRequest paramHttpServletRequest);
  
  void setLocaleContext(HttpServletRequest paramHttpServletRequest, @Nullable HttpServletResponse paramHttpServletResponse, @Nullable LocaleContext paramLocaleContext);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/LocaleContextResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */