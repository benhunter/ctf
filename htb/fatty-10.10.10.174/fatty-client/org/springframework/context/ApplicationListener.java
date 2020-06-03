package org.springframework.context;

import java.util.EventListener;

@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
  void onApplicationEvent(E paramE);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/ApplicationListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */