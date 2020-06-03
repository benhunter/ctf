package org.springframework.context.annotation;

import org.springframework.instrument.classloading.LoadTimeWeaver;

public interface LoadTimeWeavingConfigurer {
  LoadTimeWeaver getLoadTimeWeaver();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/LoadTimeWeavingConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */