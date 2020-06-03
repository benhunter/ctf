package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

public interface CacheOperationInvocationContext<O extends BasicOperation> {
  O getOperation();
  
  Object getTarget();
  
  Method getMethod();
  
  Object[] getArgs();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheOperationInvocationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */