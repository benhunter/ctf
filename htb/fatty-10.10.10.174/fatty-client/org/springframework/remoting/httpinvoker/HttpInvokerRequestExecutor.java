package org.springframework.remoting.httpinvoker;

import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

@FunctionalInterface
public interface HttpInvokerRequestExecutor {
  RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration paramHttpInvokerClientConfiguration, RemoteInvocation paramRemoteInvocation) throws Exception;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/httpinvoker/HttpInvokerRequestExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */