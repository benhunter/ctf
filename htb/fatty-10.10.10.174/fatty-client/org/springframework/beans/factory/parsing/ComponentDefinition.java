package org.springframework.beans.factory.parsing;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;

public interface ComponentDefinition extends BeanMetadataElement {
  String getName();
  
  String getDescription();
  
  BeanDefinition[] getBeanDefinitions();
  
  BeanDefinition[] getInnerBeanDefinitions();
  
  BeanReference[] getBeanReferences();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/ComponentDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */