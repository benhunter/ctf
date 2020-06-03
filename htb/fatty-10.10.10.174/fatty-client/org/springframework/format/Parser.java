package org.springframework.format;

import java.text.ParseException;
import java.util.Locale;

@FunctionalInterface
public interface Parser<T> {
  T parse(String paramString, Locale paramLocale) throws ParseException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */