package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;

public interface HierarchyEventListener {
  void addAppenderEvent(Category paramCategory, Appender paramAppender);
  
  void removeAppenderEvent(Category paramCategory, Appender paramAppender);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/spi/HierarchyEventListener.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */