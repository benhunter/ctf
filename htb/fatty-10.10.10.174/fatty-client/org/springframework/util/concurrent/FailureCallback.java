package org.springframework.util.concurrent;

@FunctionalInterface
public interface FailureCallback {
  void onFailure(Throwable paramThrowable);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/concurrent/FailureCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */