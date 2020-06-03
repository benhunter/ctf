package org.springframework.aop.aspectj.annotation;

import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.lang.Nullable;

public interface AspectJAdvisorFactory {
  boolean isAspect(Class<?> paramClass);
  
  void validate(Class<?> paramClass) throws AopConfigException;
  
  List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory paramMetadataAwareAspectInstanceFactory);
  
  @Nullable
  Advisor getAdvisor(Method paramMethod, MetadataAwareAspectInstanceFactory paramMetadataAwareAspectInstanceFactory, int paramInt, String paramString);
  
  @Nullable
  Advice getAdvice(Method paramMethod, AspectJExpressionPointcut paramAspectJExpressionPointcut, MetadataAwareAspectInstanceFactory paramMetadataAwareAspectInstanceFactory, int paramInt, String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/AspectJAdvisorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */