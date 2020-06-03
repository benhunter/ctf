package org.springframework.web.context.request.async;

import java.util.function.Consumer;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

public interface AsyncWebRequest extends NativeWebRequest {
  void setTimeout(@Nullable Long paramLong);
  
  void addTimeoutHandler(Runnable paramRunnable);
  
  void addErrorHandler(Consumer<Throwable> paramConsumer);
  
  void addCompletionHandler(Runnable paramRunnable);
  
  void startAsync();
  
  boolean isAsyncStarted();
  
  void dispatch();
  
  boolean isAsyncComplete();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/async/AsyncWebRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */