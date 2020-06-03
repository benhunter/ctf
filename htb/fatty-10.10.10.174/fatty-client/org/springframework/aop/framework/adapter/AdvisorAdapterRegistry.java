package org.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;

public interface AdvisorAdapterRegistry {
  Advisor wrap(Object paramObject) throws UnknownAdviceTypeException;
  
  MethodInterceptor[] getInterceptors(Advisor paramAdvisor) throws UnknownAdviceTypeException;
  
  void registerAdvisorAdapter(AdvisorAdapter paramAdvisorAdapter);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/adapter/AdvisorAdapterRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */