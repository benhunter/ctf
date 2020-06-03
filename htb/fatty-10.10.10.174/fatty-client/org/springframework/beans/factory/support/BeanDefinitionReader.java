package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;

public interface BeanDefinitionReader {
  BeanDefinitionRegistry getRegistry();
  
  @Nullable
  ResourceLoader getResourceLoader();
  
  @Nullable
  ClassLoader getBeanClassLoader();
  
  BeanNameGenerator getBeanNameGenerator();
  
  int loadBeanDefinitions(Resource paramResource) throws BeanDefinitionStoreException;
  
  int loadBeanDefinitions(Resource... paramVarArgs) throws BeanDefinitionStoreException;
  
  int loadBeanDefinitions(String paramString) throws BeanDefinitionStoreException;
  
  int loadBeanDefinitions(String... paramVarArgs) throws BeanDefinitionStoreException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/BeanDefinitionReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */