package org.springframework.util;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface StringValueResolver {
  @Nullable
  String resolveStringValue(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/StringValueResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */