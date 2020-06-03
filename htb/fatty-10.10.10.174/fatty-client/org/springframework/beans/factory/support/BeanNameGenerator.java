package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;

public interface BeanNameGenerator {
  String generateBeanName(BeanDefinition paramBeanDefinition, BeanDefinitionRegistry paramBeanDefinitionRegistry);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/BeanNameGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */