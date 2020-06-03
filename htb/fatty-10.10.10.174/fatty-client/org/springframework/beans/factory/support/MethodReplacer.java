package org.springframework.beans.factory.support;

import java.lang.reflect.Method;

public interface MethodReplacer {
  Object reimplement(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/MethodReplacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */