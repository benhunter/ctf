package org.aopalliance.intercept;

@FunctionalInterface
public interface MethodInterceptor extends Interceptor {
  Object invoke(MethodInvocation paramMethodInvocation) throws Throwable;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/aopalliance/intercept/MethodInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */