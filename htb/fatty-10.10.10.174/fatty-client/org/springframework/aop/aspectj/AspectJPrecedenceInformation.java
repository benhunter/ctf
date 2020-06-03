package org.springframework.aop.aspectj;

import org.springframework.core.Ordered;

public interface AspectJPrecedenceInformation extends Ordered {
  String getAspectName();
  
  int getDeclarationOrder();
  
  boolean isBeforeAdvice();
  
  boolean isAfterAdvice();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJPrecedenceInformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */