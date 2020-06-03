package org.springframework.aop.framework;

import java.lang.reflect.Method;
import java.util.List;
import org.springframework.lang.Nullable;

public interface AdvisorChainFactory {
  List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised paramAdvised, Method paramMethod, @Nullable Class<?> paramClass);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/AdvisorChainFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */