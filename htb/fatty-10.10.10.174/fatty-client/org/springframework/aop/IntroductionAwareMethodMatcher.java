package org.springframework.aop;

import java.lang.reflect.Method;

public interface IntroductionAwareMethodMatcher extends MethodMatcher {
  boolean matches(Method paramMethod, Class<?> paramClass, boolean paramBoolean);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/IntroductionAwareMethodMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */