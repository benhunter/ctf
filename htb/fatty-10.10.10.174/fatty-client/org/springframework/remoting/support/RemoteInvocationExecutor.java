package org.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;

public interface RemoteInvocationExecutor {
  Object invoke(RemoteInvocation paramRemoteInvocation, Object paramObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/RemoteInvocationExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */