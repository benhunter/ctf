package org.springframework.context.weaving;

import org.springframework.beans.factory.Aware;
import org.springframework.instrument.classloading.LoadTimeWeaver;

public interface LoadTimeWeaverAware extends Aware {
  void setLoadTimeWeaver(LoadTimeWeaver paramLoadTimeWeaver);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/weaving/LoadTimeWeaverAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */