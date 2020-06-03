package org.springframework.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {
  void setApplicationContext(ApplicationContext paramApplicationContext) throws BeansException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/ApplicationContextAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */