package org.springframework.jmx.export;

import javax.management.ObjectName;

public interface MBeanExporterListener {
  void mbeanRegistered(ObjectName paramObjectName);
  
  void mbeanUnregistered(ObjectName paramObjectName);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/MBeanExporterListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */