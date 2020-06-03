package org.springframework.aop.framework.autoproxy;

import org.springframework.aop.TargetSource;
import org.springframework.lang.Nullable;

@FunctionalInterface
public interface TargetSourceCreator {
  @Nullable
  TargetSource getTargetSource(Class<?> paramClass, String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/TargetSourceCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */