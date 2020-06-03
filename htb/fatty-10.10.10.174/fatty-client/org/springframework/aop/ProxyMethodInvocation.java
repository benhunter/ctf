package org.springframework.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;

public interface ProxyMethodInvocation extends MethodInvocation {
  Object getProxy();
  
  MethodInvocation invocableClone();
  
  MethodInvocation invocableClone(Object... paramVarArgs);
  
  void setArguments(Object... paramVarArgs);
  
  void setUserAttribute(String paramString, @Nullable Object paramObject);
  
  @Nullable
  Object getUserAttribute(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/ProxyMethodInvocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */