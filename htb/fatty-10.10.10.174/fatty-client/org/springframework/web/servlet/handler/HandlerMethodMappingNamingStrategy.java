package org.springframework.web.servlet.handler;

import org.springframework.web.method.HandlerMethod;

@FunctionalInterface
public interface HandlerMethodMappingNamingStrategy<T> {
  String getName(HandlerMethod paramHandlerMethod, T paramT);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/HandlerMethodMappingNamingStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */