package org.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

public interface RemoteInvocationFactory {
  RemoteInvocation createRemoteInvocation(MethodInvocation paramMethodInvocation);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/RemoteInvocationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */