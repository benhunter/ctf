package org.springframework.aop.target.dynamic;

public interface Refreshable {
  void refresh();
  
  long getRefreshCount();
  
  long getLastRefreshTime();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/dynamic/Refreshable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */