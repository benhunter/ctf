package org.springframework.aop;

import org.springframework.lang.Nullable;

public interface TargetSource extends TargetClassAware {
  @Nullable
  Class<?> getTargetClass();
  
  boolean isStatic();
  
  @Nullable
  Object getTarget() throws Exception;
  
  void releaseTarget(Object paramObject) throws Exception;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/TargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */