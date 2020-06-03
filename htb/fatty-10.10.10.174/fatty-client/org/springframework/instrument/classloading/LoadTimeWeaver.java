package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;

public interface LoadTimeWeaver {
  void addTransformer(ClassFileTransformer paramClassFileTransformer);
  
  ClassLoader getInstrumentableClassLoader();
  
  ClassLoader getThrowawayClassLoader();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/LoadTimeWeaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */