package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

@FunctionalInterface
public interface KeyGenerator {
  Object generate(Object paramObject, Method paramMethod, Object... paramVarArgs);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/KeyGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */