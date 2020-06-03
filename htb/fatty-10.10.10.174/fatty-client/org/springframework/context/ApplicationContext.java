package org.springframework.context;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.Nullable;

public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory, MessageSource, ApplicationEventPublisher, ResourcePatternResolver {
  @Nullable
  String getId();
  
  String getApplicationName();
  
  String getDisplayName();
  
  long getStartupDate();
  
  @Nullable
  ApplicationContext getParent();
  
  AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/ApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */