package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;

public interface RequestBodyAdvice {
  boolean supports(MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass);
  
  HttpInputMessage beforeBodyRead(HttpInputMessage paramHttpInputMessage, MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass) throws IOException;
  
  Object afterBodyRead(Object paramObject, HttpInputMessage paramHttpInputMessage, MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass);
  
  @Nullable
  Object handleEmptyBody(@Nullable Object paramObject, HttpInputMessage paramHttpInputMessage, MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/RequestBodyAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */