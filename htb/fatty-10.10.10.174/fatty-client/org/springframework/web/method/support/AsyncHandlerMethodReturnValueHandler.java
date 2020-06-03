package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

public interface AsyncHandlerMethodReturnValueHandler extends HandlerMethodReturnValueHandler {
  boolean isAsyncReturnValue(@Nullable Object paramObject, MethodParameter paramMethodParameter);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/support/AsyncHandlerMethodReturnValueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */