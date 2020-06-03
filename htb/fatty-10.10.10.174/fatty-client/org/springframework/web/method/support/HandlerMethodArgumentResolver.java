package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

public interface HandlerMethodArgumentResolver {
  boolean supportsParameter(MethodParameter paramMethodParameter);
  
  @Nullable
  Object resolveArgument(MethodParameter paramMethodParameter, @Nullable ModelAndViewContainer paramModelAndViewContainer, NativeWebRequest paramNativeWebRequest, @Nullable WebDataBinderFactory paramWebDataBinderFactory) throws Exception;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/support/HandlerMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */