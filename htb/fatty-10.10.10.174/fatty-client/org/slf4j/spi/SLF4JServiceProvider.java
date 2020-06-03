package org.slf4j.spi;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;

public interface SLF4JServiceProvider {
  ILoggerFactory getLoggerFactory();
  
  IMarkerFactory getMarkerFactory();
  
  MDCAdapter getMDCAdapter();
  
  String getRequesteApiVersion();
  
  void initialize();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/spi/SLF4JServiceProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */