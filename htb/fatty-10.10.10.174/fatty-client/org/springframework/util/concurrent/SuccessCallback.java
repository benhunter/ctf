package org.springframework.util.concurrent;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface SuccessCallback<T> {
  void onSuccess(@Nullable T paramT);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/concurrent/SuccessCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */