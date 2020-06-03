package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

public interface ApplicationEventMulticaster {
  void addApplicationListener(ApplicationListener<?> paramApplicationListener);
  
  void addApplicationListenerBean(String paramString);
  
  void removeApplicationListener(ApplicationListener<?> paramApplicationListener);
  
  void removeApplicationListenerBean(String paramString);
  
  void removeAllListeners();
  
  void multicastEvent(ApplicationEvent paramApplicationEvent);
  
  void multicastEvent(ApplicationEvent paramApplicationEvent, @Nullable ResolvableType paramResolvableType);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/ApplicationEventMulticaster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */