package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.lang.Nullable;

public interface InstantiationStrategy {
  Object instantiate(RootBeanDefinition paramRootBeanDefinition, @Nullable String paramString, BeanFactory paramBeanFactory) throws BeansException;
  
  Object instantiate(RootBeanDefinition paramRootBeanDefinition, @Nullable String paramString, BeanFactory paramBeanFactory, Constructor<?> paramConstructor, Object... paramVarArgs) throws BeansException;
  
  Object instantiate(RootBeanDefinition paramRootBeanDefinition, @Nullable String paramString, BeanFactory paramBeanFactory, @Nullable Object paramObject, Method paramMethod, Object... paramVarArgs) throws BeansException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/InstantiationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */