package org.springframework.context;

import java.util.Locale;
import org.springframework.lang.Nullable;

public interface MessageSource {
  @Nullable
  String getMessage(String paramString1, @Nullable Object[] paramArrayOfObject, @Nullable String paramString2, Locale paramLocale);
  
  String getMessage(String paramString, @Nullable Object[] paramArrayOfObject, Locale paramLocale) throws NoSuchMessageException;
  
  String getMessage(MessageSourceResolvable paramMessageSourceResolvable, Locale paramLocale) throws NoSuchMessageException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/MessageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */