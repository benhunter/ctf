package org.springframework.core.io;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface ProtocolResolver {
  @Nullable
  Resource resolve(String paramString, ResourceLoader paramResourceLoader);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/ProtocolResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */