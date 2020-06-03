package org.springframework.core.task;

import java.util.concurrent.Callable;
import org.springframework.util.concurrent.ListenableFuture;

public interface AsyncListenableTaskExecutor extends AsyncTaskExecutor {
  ListenableFuture<?> submitListenable(Runnable paramRunnable);
  
  <T> ListenableFuture<T> submitListenable(Callable<T> paramCallable);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/task/AsyncListenableTaskExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */