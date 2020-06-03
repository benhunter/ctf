package org.springframework.aop.aspectj;

import org.springframework.aop.PointcutAdvisor;

public interface InstantiationModelAwarePointcutAdvisor extends PointcutAdvisor {
  boolean isLazy();
  
  boolean isAdviceInstantiated();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/InstantiationModelAwarePointcutAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */