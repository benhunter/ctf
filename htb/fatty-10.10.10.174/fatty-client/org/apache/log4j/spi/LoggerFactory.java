package org.apache.log4j.spi;

import org.apache.log4j.Logger;

public interface LoggerFactory {
  Logger makeNewLoggerInstance(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/spi/LoggerFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */