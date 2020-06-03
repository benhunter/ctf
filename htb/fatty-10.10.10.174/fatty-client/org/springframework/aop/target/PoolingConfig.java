package org.springframework.aop.target;

public interface PoolingConfig {
  int getMaxSize();
  
  int getActiveCount() throws UnsupportedOperationException;
  
  int getIdleCount() throws UnsupportedOperationException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/PoolingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */