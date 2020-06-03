package org.aopalliance.intercept;

public interface ConstructorInterceptor extends Interceptor {
  Object construct(ConstructorInvocation paramConstructorInvocation) throws Throwable;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/aopalliance/intercept/ConstructorInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */