package org.aopalliance.intercept;

import java.lang.reflect.Constructor;

public interface ConstructorInvocation extends Invocation {
  Constructor<?> getConstructor();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/aopalliance/intercept/ConstructorInvocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */