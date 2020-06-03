package org.springframework.aop.framework;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.TargetSource;

public interface Advised extends TargetClassAware {
  boolean isFrozen();
  
  boolean isProxyTargetClass();
  
  Class<?>[] getProxiedInterfaces();
  
  boolean isInterfaceProxied(Class<?> paramClass);
  
  void setTargetSource(TargetSource paramTargetSource);
  
  TargetSource getTargetSource();
  
  void setExposeProxy(boolean paramBoolean);
  
  boolean isExposeProxy();
  
  void setPreFiltered(boolean paramBoolean);
  
  boolean isPreFiltered();
  
  Advisor[] getAdvisors();
  
  void addAdvisor(Advisor paramAdvisor) throws AopConfigException;
  
  void addAdvisor(int paramInt, Advisor paramAdvisor) throws AopConfigException;
  
  boolean removeAdvisor(Advisor paramAdvisor);
  
  void removeAdvisor(int paramInt) throws AopConfigException;
  
  int indexOf(Advisor paramAdvisor);
  
  boolean replaceAdvisor(Advisor paramAdvisor1, Advisor paramAdvisor2) throws AopConfigException;
  
  void addAdvice(Advice paramAdvice) throws AopConfigException;
  
  void addAdvice(int paramInt, Advice paramAdvice) throws AopConfigException;
  
  boolean removeAdvice(Advice paramAdvice);
  
  int indexOf(Advice paramAdvice);
  
  String toProxyConfigString();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/Advised.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */