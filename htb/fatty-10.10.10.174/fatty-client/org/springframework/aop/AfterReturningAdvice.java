package org.springframework.aop;

import java.lang.reflect.Method;
import org.springframework.lang.Nullable;

public interface AfterReturningAdvice extends AfterAdvice {
  void afterReturning(@Nullable Object paramObject1, Method paramMethod, Object[] paramArrayOfObject, @Nullable Object paramObject2) throws Throwable;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/AfterReturningAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */