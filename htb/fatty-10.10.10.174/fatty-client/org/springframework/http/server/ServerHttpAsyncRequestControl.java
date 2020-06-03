package org.springframework.http.server;

public interface ServerHttpAsyncRequestControl {
  void start();
  
  void start(long paramLong);
  
  boolean isStarted();
  
  void complete();
  
  boolean isCompleted();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/ServerHttpAsyncRequestControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */