package org.springframework.aop.support;

import org.springframework.aop.Pointcut;
import org.springframework.lang.Nullable;

public interface ExpressionPointcut extends Pointcut {
  @Nullable
  String getExpression();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/ExpressionPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */