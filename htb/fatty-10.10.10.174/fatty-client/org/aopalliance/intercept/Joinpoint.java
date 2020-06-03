package org.aopalliance.intercept;

import java.lang.reflect.AccessibleObject;

public interface Joinpoint {
  Object proceed() throws Throwable;
  
  Object getThis();
  
  AccessibleObject getStaticPart();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/aopalliance/intercept/Joinpoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */