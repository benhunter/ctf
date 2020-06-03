package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;

public interface ResponseBodyAdvice<T> {
  boolean supports(MethodParameter paramMethodParameter, Class<? extends HttpMessageConverter<?>> paramClass);
  
  @Nullable
  T beforeBodyWrite(@Nullable T paramT, MethodParameter paramMethodParameter, MediaType paramMediaType, Class<? extends HttpMessageConverter<?>> paramClass, ServerHttpRequest paramServerHttpRequest, ServerHttpResponse paramServerHttpResponse);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ResponseBodyAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */