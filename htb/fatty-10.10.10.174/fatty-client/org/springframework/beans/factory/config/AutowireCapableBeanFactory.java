package org.springframework.beans.factory.config;

import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.lang.Nullable;

public interface AutowireCapableBeanFactory extends BeanFactory {
  public static final int AUTOWIRE_NO = 0;
  
  public static final int AUTOWIRE_BY_NAME = 1;
  
  public static final int AUTOWIRE_BY_TYPE = 2;
  
  public static final int AUTOWIRE_CONSTRUCTOR = 3;
  
  @Deprecated
  public static final int AUTOWIRE_AUTODETECT = 4;
  
  public static final String ORIGINAL_INSTANCE_SUFFIX = ".ORIGINAL";
  
  <T> T createBean(Class<T> paramClass) throws BeansException;
  
  void autowireBean(Object paramObject) throws BeansException;
  
  Object configureBean(Object paramObject, String paramString) throws BeansException;
  
  Object createBean(Class<?> paramClass, int paramInt, boolean paramBoolean) throws BeansException;
  
  Object autowire(Class<?> paramClass, int paramInt, boolean paramBoolean) throws BeansException;
  
  void autowireBeanProperties(Object paramObject, int paramInt, boolean paramBoolean) throws BeansException;
  
  void applyBeanPropertyValues(Object paramObject, String paramString) throws BeansException;
  
  Object initializeBean(Object paramObject, String paramString) throws BeansException;
  
  Object applyBeanPostProcessorsBeforeInitialization(Object paramObject, String paramString) throws BeansException;
  
  Object applyBeanPostProcessorsAfterInitialization(Object paramObject, String paramString) throws BeansException;
  
  void destroyBean(Object paramObject);
  
  <T> NamedBeanHolder<T> resolveNamedBean(Class<T> paramClass) throws BeansException;
  
  Object resolveBeanByName(String paramString, DependencyDescriptor paramDependencyDescriptor) throws BeansException;
  
  @Nullable
  Object resolveDependency(DependencyDescriptor paramDependencyDescriptor, @Nullable String paramString) throws BeansException;
  
  @Nullable
  Object resolveDependency(DependencyDescriptor paramDependencyDescriptor, @Nullable String paramString, @Nullable Set<String> paramSet, @Nullable TypeConverter paramTypeConverter) throws BeansException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/AutowireCapableBeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */