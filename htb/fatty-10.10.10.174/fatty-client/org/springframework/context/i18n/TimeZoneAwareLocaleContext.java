package org.springframework.context.i18n;

import java.util.TimeZone;
import org.springframework.lang.Nullable;

public interface TimeZoneAwareLocaleContext extends LocaleContext {
  @Nullable
  TimeZone getTimeZone();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/i18n/TimeZoneAwareLocaleContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */