package org.slf4j.event;

import org.slf4j.Marker;

public interface LoggingEvent {
  Level getLevel();
  
  Marker getMarker();
  
  String getLoggerName();
  
  String getMessage();
  
  String getThreadName();
  
  Object[] getArgumentArray();
  
  long getTimeStamp();
  
  Throwable getThrowable();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/event/LoggingEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */