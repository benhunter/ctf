package org.springframework.format;

import java.util.Locale;

@FunctionalInterface
public interface Printer<T> {
  String print(T paramT, Locale paramLocale);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/Printer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */