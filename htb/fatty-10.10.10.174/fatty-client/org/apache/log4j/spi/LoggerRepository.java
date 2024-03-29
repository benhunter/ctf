package org.apache.log4j.spi;

import java.util.Enumeration;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public interface LoggerRepository {
  void addHierarchyEventListener(HierarchyEventListener paramHierarchyEventListener);
  
  boolean isDisabled(int paramInt);
  
  void setThreshold(Level paramLevel);
  
  void setThreshold(String paramString);
  
  void emitNoAppenderWarning(Category paramCategory);
  
  Level getThreshold();
  
  Logger getLogger(String paramString);
  
  Logger getLogger(String paramString, LoggerFactory paramLoggerFactory);
  
  Logger getRootLogger();
  
  Logger exists(String paramString);
  
  void shutdown();
  
  Enumeration getCurrentLoggers();
  
  Enumeration getCurrentCategories();
  
  void fireAddAppenderEvent(Category paramCategory, Appender paramAppender);
  
  void resetConfiguration();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/spi/LoggerRepository.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */