package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanPostProcessor;

public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {
  void postProcessMergedBeanDefinition(RootBeanDefinition paramRootBeanDefinition, Class<?> paramClass, String paramString);
  
  default void resetBeanDefinition(String beanName) {}
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/MergedBeanDefinitionPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */