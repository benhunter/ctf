package org.springframework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

public interface ListableBeanFactory extends BeanFactory {
  boolean containsBeanDefinition(String paramString);
  
  int getBeanDefinitionCount();
  
  String[] getBeanDefinitionNames();
  
  String[] getBeanNamesForType(ResolvableType paramResolvableType);
  
  String[] getBeanNamesForType(@Nullable Class<?> paramClass);
  
  String[] getBeanNamesForType(@Nullable Class<?> paramClass, boolean paramBoolean1, boolean paramBoolean2);
  
  <T> Map<String, T> getBeansOfType(@Nullable Class<T> paramClass) throws BeansException;
  
  <T> Map<String, T> getBeansOfType(@Nullable Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2) throws BeansException;
  
  String[] getBeanNamesForAnnotation(Class<? extends Annotation> paramClass);
  
  Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> paramClass) throws BeansException;
  
  @Nullable
  <A extends Annotation> A findAnnotationOnBean(String paramString, Class<A> paramClass) throws NoSuchBeanDefinitionException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/ListableBeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */