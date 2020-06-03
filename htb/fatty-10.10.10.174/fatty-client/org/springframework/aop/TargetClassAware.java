package org.springframework.aop;

import org.springframework.lang.Nullable;

public interface TargetClassAware {
  @Nullable
  Class<?> getTargetClass();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/TargetClassAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */