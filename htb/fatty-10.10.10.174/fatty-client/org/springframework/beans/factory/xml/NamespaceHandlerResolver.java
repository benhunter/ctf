package org.springframework.beans.factory.xml;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface NamespaceHandlerResolver {
  @Nullable
  NamespaceHandler resolve(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/NamespaceHandlerResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */