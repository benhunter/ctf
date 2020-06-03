package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

public interface InvocationHandler extends Callback {
  Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cglib/proxy/InvocationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */