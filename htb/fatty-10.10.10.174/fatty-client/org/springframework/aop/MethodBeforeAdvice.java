package org.springframework.aop;

import java.lang.reflect.Method;
import org.springframework.lang.Nullable;

public interface MethodBeforeAdvice extends BeforeAdvice {
  void before(Method paramMethod, Object[] paramArrayOfObject, @Nullable Object paramObject) throws Throwable;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/MethodBeforeAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */