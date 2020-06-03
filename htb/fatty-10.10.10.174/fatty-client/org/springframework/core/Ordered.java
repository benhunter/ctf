package org.springframework.core;

public interface Ordered {
  public static final int HIGHEST_PRECEDENCE = -2147483648;
  
  public static final int LOWEST_PRECEDENCE = 2147483647;
  
  int getOrder();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/Ordered.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */