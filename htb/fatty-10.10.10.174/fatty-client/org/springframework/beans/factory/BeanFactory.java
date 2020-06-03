package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

public interface BeanFactory {
  public static final String FACTORY_BEAN_PREFIX = "&";
  
  Object getBean(String paramString) throws BeansException;
  
  <T> T getBean(String paramString, Class<T> paramClass) throws BeansException;
  
  Object getBean(String paramString, Object... paramVarArgs) throws BeansException;
  
  <T> T getBean(Class<T> paramClass) throws BeansException;
  
  <T> T getBean(Class<T> paramClass, Object... paramVarArgs) throws BeansException;
  
  <T> ObjectProvider<T> getBeanProvider(Class<T> paramClass);
  
  <T> ObjectProvider<T> getBeanProvider(ResolvableType paramResolvableType);
  
  boolean containsBean(String paramString);
  
  boolean isSingleton(String paramString) throws NoSuchBeanDefinitionException;
  
  boolean isPrototype(String paramString) throws NoSuchBeanDefinitionException;
  
  boolean isTypeMatch(String paramString, ResolvableType paramResolvableType) throws NoSuchBeanDefinitionException;
  
  boolean isTypeMatch(String paramString, Class<?> paramClass) throws NoSuchBeanDefinitionException;
  
  @Nullable
  Class<?> getType(String paramString) throws NoSuchBeanDefinitionException;
  
  String[] getAliases(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/BeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */