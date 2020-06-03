package org.springframework.web.servlet;

import java.util.Locale;
import org.springframework.lang.Nullable;

public interface ViewResolver {
  @Nullable
  View resolveViewName(String paramString, Locale paramLocale) throws Exception;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/ViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */