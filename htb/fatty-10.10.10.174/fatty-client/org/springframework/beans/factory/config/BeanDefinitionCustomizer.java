package org.springframework.beans.factory.config;

@FunctionalInterface
public interface BeanDefinitionCustomizer {
  void customize(BeanDefinition paramBeanDefinition);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/BeanDefinitionCustomizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */