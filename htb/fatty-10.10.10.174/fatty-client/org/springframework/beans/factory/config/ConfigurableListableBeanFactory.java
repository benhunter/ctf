package org.springframework.beans.factory.config;

import java.util.Iterator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.lang.Nullable;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {
  void ignoreDependencyType(Class<?> paramClass);
  
  void ignoreDependencyInterface(Class<?> paramClass);
  
  void registerResolvableDependency(Class<?> paramClass, @Nullable Object paramObject);
  
  boolean isAutowireCandidate(String paramString, DependencyDescriptor paramDependencyDescriptor) throws NoSuchBeanDefinitionException;
  
  BeanDefinition getBeanDefinition(String paramString) throws NoSuchBeanDefinitionException;
  
  Iterator<String> getBeanNamesIterator();
  
  void clearMetadataCache();
  
  void freezeConfiguration();
  
  boolean isConfigurationFrozen();
  
  void preInstantiateSingletons() throws BeansException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/ConfigurableListableBeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */