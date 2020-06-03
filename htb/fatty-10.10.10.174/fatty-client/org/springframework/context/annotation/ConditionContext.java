package org.springframework.context.annotation;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;

public interface ConditionContext {
  BeanDefinitionRegistry getRegistry();
  
  @Nullable
  ConfigurableListableBeanFactory getBeanFactory();
  
  Environment getEnvironment();
  
  ResourceLoader getResourceLoader();
  
  @Nullable
  ClassLoader getClassLoader();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ConditionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */