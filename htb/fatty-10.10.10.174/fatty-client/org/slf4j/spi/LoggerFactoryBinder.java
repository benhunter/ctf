package org.slf4j.spi;

import org.slf4j.ILoggerFactory;

public interface LoggerFactoryBinder {
  ILoggerFactory getLoggerFactory();
  
  String getLoggerFactoryClassStr();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/spi/LoggerFactoryBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */