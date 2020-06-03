package org.springframework.beans.factory.config;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
  public static final String SCOPE_SINGLETON = "singleton";
  
  public static final String SCOPE_PROTOTYPE = "prototype";
  
  void setParentBeanFactory(BeanFactory paramBeanFactory) throws IllegalStateException;
  
  void setBeanClassLoader(@Nullable ClassLoader paramClassLoader);
  
  @Nullable
  ClassLoader getBeanClassLoader();
  
  void setTempClassLoader(@Nullable ClassLoader paramClassLoader);
  
  @Nullable
  ClassLoader getTempClassLoader();
  
  void setCacheBeanMetadata(boolean paramBoolean);
  
  boolean isCacheBeanMetadata();
  
  void setBeanExpressionResolver(@Nullable BeanExpressionResolver paramBeanExpressionResolver);
  
  @Nullable
  BeanExpressionResolver getBeanExpressionResolver();
  
  void setConversionService(@Nullable ConversionService paramConversionService);
  
  @Nullable
  ConversionService getConversionService();
  
  void addPropertyEditorRegistrar(PropertyEditorRegistrar paramPropertyEditorRegistrar);
  
  void registerCustomEditor(Class<?> paramClass, Class<? extends PropertyEditor> paramClass1);
  
  void copyRegisteredEditorsTo(PropertyEditorRegistry paramPropertyEditorRegistry);
  
  void setTypeConverter(TypeConverter paramTypeConverter);
  
  TypeConverter getTypeConverter();
  
  void addEmbeddedValueResolver(StringValueResolver paramStringValueResolver);
  
  boolean hasEmbeddedValueResolver();
  
  @Nullable
  String resolveEmbeddedValue(String paramString);
  
  void addBeanPostProcessor(BeanPostProcessor paramBeanPostProcessor);
  
  int getBeanPostProcessorCount();
  
  void registerScope(String paramString, Scope paramScope);
  
  String[] getRegisteredScopeNames();
  
  @Nullable
  Scope getRegisteredScope(String paramString);
  
  AccessControlContext getAccessControlContext();
  
  void copyConfigurationFrom(ConfigurableBeanFactory paramConfigurableBeanFactory);
  
  void registerAlias(String paramString1, String paramString2) throws BeanDefinitionStoreException;
  
  void resolveAliases(StringValueResolver paramStringValueResolver);
  
  BeanDefinition getMergedBeanDefinition(String paramString) throws NoSuchBeanDefinitionException;
  
  boolean isFactoryBean(String paramString) throws NoSuchBeanDefinitionException;
  
  void setCurrentlyInCreation(String paramString, boolean paramBoolean);
  
  boolean isCurrentlyInCreation(String paramString);
  
  void registerDependentBean(String paramString1, String paramString2);
  
  String[] getDependentBeans(String paramString);
  
  String[] getDependenciesForBean(String paramString);
  
  void destroyBean(String paramString, Object paramObject);
  
  void destroyScopedBean(String paramString);
  
  void destroySingletons();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/ConfigurableBeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */