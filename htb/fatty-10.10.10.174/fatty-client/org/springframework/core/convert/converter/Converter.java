package org.springframework.core.convert.converter;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface Converter<S, T> {
  @Nullable
  T convert(S paramS);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/converter/Converter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */