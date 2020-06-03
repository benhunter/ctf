package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LoggingEvent;

public interface RewritePolicy {
  LoggingEvent rewrite(LoggingEvent paramLoggingEvent);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/rewrite/RewritePolicy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */