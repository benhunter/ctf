package org.springframework.context;

public interface Lifecycle {
  void start();
  
  void stop();
  
  boolean isRunning();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/Lifecycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */