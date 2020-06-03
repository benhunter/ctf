package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public interface BeanFactoryAware extends Aware {
  void setBeanFactory(BeanFactory paramBeanFactory) throws BeansException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/BeanFactoryAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */