package org.springframework.context;

public interface LifecycleProcessor extends Lifecycle {
  void onRefresh();
  
  void onClose();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/LifecycleProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */