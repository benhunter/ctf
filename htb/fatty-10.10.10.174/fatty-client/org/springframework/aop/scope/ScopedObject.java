package org.springframework.aop.scope;

import org.springframework.aop.RawTargetAccess;

public interface ScopedObject extends RawTargetAccess {
  Object getTargetObject();
  
  void removeFromScope();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/scope/ScopedObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */