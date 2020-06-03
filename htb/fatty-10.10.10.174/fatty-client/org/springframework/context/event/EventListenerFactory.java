package org.springframework.context.event;

import java.lang.reflect.Method;
import org.springframework.context.ApplicationListener;

public interface EventListenerFactory {
  boolean supportsMethod(Method paramMethod);
  
  ApplicationListener<?> createApplicationListener(String paramString, Class<?> paramClass, Method paramMethod);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/EventListenerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */