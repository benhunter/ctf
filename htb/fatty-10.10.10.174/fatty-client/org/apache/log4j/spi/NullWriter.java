package org.apache.log4j.spi;

import java.io.Writer;

class NullWriter extends Writer {
  public void close() {}
  
  public void flush() {}
  
  public void write(char[] cbuf, int off, int len) {}
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/spi/NullWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */