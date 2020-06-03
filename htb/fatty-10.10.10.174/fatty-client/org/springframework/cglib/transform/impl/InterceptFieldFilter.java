package org.springframework.cglib.transform.impl;

import org.springframework.asm.Type;

public interface InterceptFieldFilter {
  boolean acceptRead(Type paramType, String paramString);
  
  boolean acceptWrite(Type paramType, String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cglib/transform/impl/InterceptFieldFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */