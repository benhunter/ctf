package org.springframework.context;

import java.io.Closeable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.lang.Nullable;

public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {
  public static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
  
  public static final String CONVERSION_SERVICE_BEAN_NAME = "conversionService";
  
  public static final String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver";
  
  public static final String ENVIRONMENT_BEAN_NAME = "environment";
  
  public static final String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";
  
  public static final String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";
  
  void setId(String paramString);
  
  void setParent(@Nullable ApplicationContext paramApplicationContext);
  
  void setEnvironment(ConfigurableEnvironment paramConfigurableEnvironment);
  
  ConfigurableEnvironment getEnvironment();
  
  void addBeanFactoryPostProcessor(BeanFactoryPostProcessor paramBeanFactoryPostProcessor);
  
  void addApplicationListener(ApplicationListener<?> paramApplicationListener);
  
  void addProtocolResolver(ProtocolResolver paramProtocolResolver);
  
  void refresh() throws BeansException, IllegalStateException;
  
  void registerShutdownHook();
  
  void close();
  
  boolean isActive();
  
  ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/ConfigurableApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */