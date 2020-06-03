package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.lang.Nullable;

public interface AnnotatedBeanDefinition extends BeanDefinition {
  AnnotationMetadata getMetadata();
  
  @Nullable
  MethodMetadata getFactoryMethodMetadata();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/AnnotatedBeanDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */