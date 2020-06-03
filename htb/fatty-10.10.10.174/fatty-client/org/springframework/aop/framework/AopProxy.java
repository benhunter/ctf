package org.springframework.aop.framework;

import org.springframework.lang.Nullable;

public interface AopProxy {
  Object getProxy();
  
  Object getProxy(@Nullable ClassLoader paramClassLoader);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/AopProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */